package basicmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import basicmod.powers.CurrentStancePower; // 你的架势Power路径

import java.util.ArrayList;

public class StomachImpactPower extends AbstractPower {
    public static final String POWER_ID = "soul-five:StomachImpactPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


    private ArrayList<AbstractMonster> targets; // 存储需要攻击的目标列表
    private boolean isAOE = false; // 是否触发全体攻击
    private int baseDmg; // 基础伤害

    public StomachImpactPower(AbstractCreature owner, AbstractMonster target, int damageAmount, boolean isAOE) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 1; // 层数仅用于显示图标，实际逻辑由列表控制
        this.type = PowerType.BUFF; // 这是一个对自己有利的状态
        this.targets = new ArrayList<>();
        this.baseDmg = damageAmount;

        // 初始化逻辑
        handleStackLogic(target, isAOE);
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/StomachImpact.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/StomachImpact.png"), 0, 0, 32, 32
        );

        updateDescription();
        updateDescription();
    }

    // 当多次获得该能力时（比如一回合打出两张胃袋冲击），调用此方法
    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        // 注意：这里我们无法直接获取 stackPower 时的参数，
        // 所以我们需要一种特殊的 ApplyPowerAction 或者在 use 中处理。
        // 为了简化，我们假设 stackPower 时只增加层数，
        // 但为了支持“打两张单体卡攻击不同敌人”，我们需要更复杂的逻辑。
        // *但在Slay the Spire的标准Mod开发中，通常新建一个Power实例会自动合并*
        // 为了让代码健壮，我们在构造函数里处理了初始逻辑，
        // 如果游戏引擎调用 stackPower，说明是同类合并，我们需要手动更新 targets。
        // 这是一个简化的实现，如果需要完美支持多目标，建议在 Card 的 use 方法里
        // 先判断玩家是否有此 Power，如果有，直接获取该 Power 实例并调用自定义方法 addTarget。
    }

    // 自定义方法，供 Card 类调用以添加目标（如果 Power 已存在）
    // 修改 Card 类的 use 方法逻辑见下方补充
    public void addTarget(AbstractMonster m, boolean aoe) {
        handleStackLogic(m, aoe);
        updateDescription();
    }

    private void handleStackLogic(AbstractMonster m, boolean aoe) {
        if (aoe) {
            this.isAOE = true;
        } else if (m != null) {
            this.targets.add(m);
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            flash();

            // 1. 计算伤害
            int finalDamage = this.baseDmg;

            // 2. 检查架势
            if (owner.hasPower(CurrentStancePower.POWER_ID)) {
                CurrentStancePower stance = (CurrentStancePower) owner.getPower(CurrentStancePower.POWER_ID);
                // 假设你的枚举是 IRON
                if (stance.getCurrentStance() == CurrentStancePower.Stance.IRON) {
                    finalDamage = owner.currentBlock;
                }
            }

            // 3. 执行伤害
            if (this.isAOE) {
                // 全体伤害
                addToBot(new DamageAllEnemiesAction(owner,
                        DamageInfo.createDamageMatrix(finalDamage, true),
                        DamageInfo.DamageType.NORMAL,
                        AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            } else {
                // 单体伤害（遍历所有存储的目标）
                for (AbstractMonster m : targets) {
                    if (!m.isDeadOrEscaped()) {
                        addToBot(new DamageAction(m,
                                new DamageInfo(owner, finalDamage, DamageInfo.DamageType.NORMAL),
                                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    }
                }
            }

            // 4. 移除能力（因为是一次性的）
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public void updateDescription() {
        if (isAOE) {
            description = DESCRIPTIONS[0] + baseDmg + DESCRIPTIONS[2]; // 全体描述
        } else {
            description = DESCRIPTIONS[0] + baseDmg + DESCRIPTIONS[1]; // 单体描述
        }
        // 可以在这里动态添加 "若处于韧狗架势，伤害为当前格挡值" 的提示
    }
}