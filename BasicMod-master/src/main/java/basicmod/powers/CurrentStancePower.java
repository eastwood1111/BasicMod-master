package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.LinkedList;
import java.util.HashMap;

public class CurrentStancePower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:CurrentStancePower";
    public static final String NAME = "当前架势";
    public static final String[] DESCRIPTIONS = {"当前架势："};

    public enum Stance {SWORD, SHIELD, GREAT_SWORD}

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
        boolean dualMode = owner.hasPower(TwoHandsPower.POWER_ID);

        if (!dualMode) {
            // 单架势模式：移除全部旧架势
            while (!stanceQueue.isEmpty()) {
                Stance old = stanceQueue.removeFirst();
                int oldAmt = stanceAmounts.remove(old);
                removeStanceEffect(old, oldAmt);
            }
        } else {
            // 双架势模式：最多两个
            if (stanceQueue.size() >= 2) {
                // 移除队头
                Stance oldest = stanceQueue.removeFirst();
                int oldAmt = stanceAmounts.remove(oldest);
                removeStanceEffect(oldest, oldAmt);
            }
        }

        // 添加新架势到队尾
        stanceQueue.addLast(newStance);
        stanceAmounts.put(newStance, amount);
        applyStanceEffect(newStance, amount);

        updateDescription();
    }

    private void applyStanceEffect(Stance stance, int amount) {
        switch (stance) {
            case SWORD:
            case GREAT_SWORD:
                addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, amount)));
                break;
            case SHIELD:
                addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, amount)));
                break;
        }
    }

    private void removeStanceEffect(Stance stance, int amount) {
        switch (stance) {
            case SWORD:
            case GREAT_SWORD:
                addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, -amount)));
                break;
            case SHIELD:
                addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, -amount)));
                break;
        }
    }

    public Stance getCurrentStance() {
        if (stanceQueue.isEmpty()) return null;
        return stanceQueue.getLast(); // 队尾为主架势
    }

    public Stance getSecondaryStance() {
        if (stanceQueue.size() < 2) return null;
        return stanceQueue.getFirst(); // 队头为次架势
    }

    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder(DESCRIPTIONS[0]);
        if (!stanceQueue.isEmpty()) sb.append("主：").append(stanceToText(getCurrentStance()));
        if (stanceQueue.size() >= 2) sb.append("，次：").append(stanceToText(getSecondaryStance()));
        this.description = sb.toString();
    }

    private String stanceToText(Stance s) {
        if (s == null) return "无";
        switch (s) {
            case SWORD: return "剑架势";
            case SHIELD: return "盾架势";
            case GREAT_SWORD: return "大剑架势";
            default: return "无";
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new CurrentStancePower(owner);
    }
}
