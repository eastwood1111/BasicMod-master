package basicmod.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.BasicMod.makeID;

public class LowHpDiscountRelic extends BaseRelic {
    public static final String ID = makeID("LowHpDiscountRelic");

    // 用于记录本回合是否已经触发过
    private boolean triggeredThisTurn = false;

    public LowHpDiscountRelic() {
        // 设置为 COMMON（普通）级别
        super(ID, "myrelic", RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public void atTurnStart() {
        // 每回合开始时重置触发状态
        this.triggeredThisTurn = false;

        // 如果生命值低于 50%，开启发光提示
        if (isHealthBelowHalf()) {
            this.beginLongPulse();
        }
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        // 核心判断：
        // 1. 本回合还没触发过
        // 2. 玩家生命值低于 50%
        if (!this.triggeredThisTurn && isHealthBelowHalf()) {
            this.triggeredThisTurn = true;
            this.flash();
            this.stopPulse(); // 触发后停止发光

            // 在玩家上方显示遗物图标
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            // 将当前打出的这张牌设为免费
            c.freeToPlayOnce = true;
        }
    }

    /**
     * 判断玩家当前生命是否低于 50%
     */
    private boolean isHealthBelowHalf() {
        return AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth / 2.0F;
    }

    @Override
    public void onVictory() {
        this.stopPulse(); // 战斗结束停止发光
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new LowHpDiscountRelic();
    }
}