package basicmod.relics;

import basicmod.Enums; // 确保导入了你的 Enums 类
import basicmod.charater.MyCharacter;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.BasicMod.makeID;

public class StanceEnergyBossRelic extends BaseRelic {
    public static final String ID = makeID("StanceEnergyBossRelic");

    private boolean triggeredThisTurn = false;

    public StanceEnergyBossRelic() {
        super(ID, "stance_energy_boss", // 对应 resources/images/relics/stance_energy_boss.png
                MyCharacter.Meta.CARD_COLOR,
                RelicTier.BOSS,
                LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStart() {
        // 每回合开始重置状态
        this.triggeredThisTurn = false;
        this.grayscale = false;
    }

    @Override
    public void onUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        // 使用 Tag 判断：如果卡牌拥有 STANCE 标签且本回合尚未触发
        if (!this.triggeredThisTurn && card.hasTag(Enums.STANCE)) {
            this.triggeredThisTurn = true;
            this.flash();

            // 视觉反馈：在玩家上方显示遗物图标
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            // 获得 1 点能量
            addToBot(new GainEnergyAction(1));

            // 变灰表示本回合已消耗
            this.grayscale = true;
        }
    }

    @Override
    public void onVictory() {
        this.grayscale = false;
        this.triggeredThisTurn = false;
    }

    @Override
    public void atPreBattle() {
        this.grayscale = false;
        this.triggeredThisTurn = false;
    }

    @Override
    public void onPlayerEndTurn() {
        // 回合结束时恢复颜色，提醒玩家下回合又可以用了
        this.grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        // 请在 RelicStrings.json 中配置 DESCRIPTIONS[0]
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new StanceEnergyBossRelic();
    }
}