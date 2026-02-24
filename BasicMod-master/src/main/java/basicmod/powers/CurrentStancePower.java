package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.LinkedList;
import java.util.HashMap;

public class CurrentStancePower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "soul-five:CurrentStancePower";
    public static final String NAME = "当前架势";
    public static final String[] DESCRIPTIONS = {"当前架势："};

    public enum Stance {SWORD, SHIELD, GREAT_SWORD, MAGIC, IRON}

    // 建议保持 private，通过下面的 public 方法访问
    private LinkedList<Stance> stanceQueue = new LinkedList<>();
    private HashMap<Stance, Integer> stanceAmounts = new HashMap<>();

    public CurrentStancePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    public void switchStance(Stance newStance, int amount) {
        // 1. 判断是否开启双持模式
        boolean dualMode = owner.hasPower(TwoHandsPower.POWER_ID);

        if (!dualMode) {
            // 单架势模式：移除全部旧架势
            while (!stanceQueue.isEmpty()) {
                Stance old = stanceQueue.removeFirst();
                Integer oldAmt = stanceAmounts.remove(old);
                if (oldAmt != null) removeStanceEffect(old, oldAmt);
            }
        } else {
            // 双架势模式：如果已经有两个了，移除最旧的一个（队头）
            if (stanceQueue.size() >= 2) {
                Stance oldest = stanceQueue.removeFirst();
                Integer oldAmt = stanceAmounts.remove(oldest);
                if (oldAmt != null) removeStanceEffect(oldest, oldAmt);
            }
        }

        // 2. 添加新架势到队尾（队尾永远是当前激活的主架势）
        stanceQueue.addLast(newStance);
        stanceAmounts.put(newStance, amount);
        applyStanceEffect(newStance, amount);

        // 3. --- 新增：触发“架势抽牌”能力 ---
        if (this.owner.hasPower(StanceDrawPower.POWER_ID)) {
            AbstractPower p = this.owner.getPower(StanceDrawPower.POWER_ID);
            if (p instanceof StanceDrawPower) {
                ((StanceDrawPower) p).onStanceTrigger();
            }

        }
        if (this.owner.hasPower(StanceDefendPower.POWER_ID)) {
            AbstractPower p = this.owner.getPower(StanceDefendPower.POWER_ID);
            if (p instanceof StanceDefendPower) {
                ((StanceDefendPower) p).onStanceTrigger();
            }
        }

        updateDescription();
    }

    private void applyStanceEffect(Stance stance, int amount) {
        switch (stance) {
            case SWORD:
            case GREAT_SWORD:
                // 关键修正：必须在最后多加一个 amount 参数
                addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount), amount));
                break;
            case SHIELD:
                addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, amount), amount));
                break;
            case IRON:
                // 韧狗加成（金属化）
                addToBot(new ApplyPowerAction(owner, owner, new com.megacrit.cardcrawl.powers.MetallicizePower(owner, amount), amount));
                break;
        }
    }

    private void removeStanceEffect(Stance stance, int amount) {
        switch (stance) {
            case SWORD:
            case GREAT_SWORD:
                // 移除时也要传 -amount
                addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, -amount), -amount));
                break;
            case SHIELD:
                addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, -amount), -amount));
                break;
            case IRON:
                addToBot(new ApplyPowerAction(owner, owner, new com.megacrit.cardcrawl.powers.MetallicizePower(owner, -amount), -amount));
                break;
        }
    }
    // --- 确保这两个方法是 PUBLIC 的，这样卡牌类才能调用 ---

    /** 获取当前主架势（最后进入的架势） */
    public Stance getCurrentStance() {
        if (stanceQueue.isEmpty()) return null;
        return stanceQueue.getLast();
    }

    /** 获取次要架势（双持模式下较早进入的架势） */
    public Stance getSecondaryStance() {
        if (stanceQueue.size() < 2) return null;
        return stanceQueue.getFirst();
    }

    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder(DESCRIPTIONS[0]);
        if (!stanceQueue.isEmpty()) {
            sb.append(" #y主：").append(stanceToText(getCurrentStance()));
        }
        if (stanceQueue.size() >= 2) {
            sb.append("， #y次：").append(stanceToText(getSecondaryStance()));
        }
        this.description = sb.toString();
    }

    private String stanceToText(Stance s) {
        if (s == null) return "无";
        switch (s) {
            case SWORD: return "剑";
            case SHIELD: return "盾";
            case GREAT_SWORD: return "大剑";
            case MAGIC: return "祷告/魔法";
            case IRON: return "韧狗";
            default: return "无";
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new CurrentStancePower(owner);
    }
}