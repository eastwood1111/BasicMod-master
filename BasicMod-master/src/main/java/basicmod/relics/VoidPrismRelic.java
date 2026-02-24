package basicmod.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.BasicMod.makeID;

public class VoidPrismRelic extends BaseRelic {
    public static final String ID = makeID("VoidPrismRelic");
    private static final int ENERGY_AMT = 1;
    // 触发阈值
    private static final int TRIGGER_TURN = 3;

    public VoidPrismRelic() {
        super(ID, "void_prism", RelicTier.BOSS, LandingSound.MAGICAL);
        // 初始化计数器为 0，这个值会随存档保存
        this.counter = 0;
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster += ENERGY_AMT;
        if (this.counter < 0) this.counter = 0;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster -= ENERGY_AMT;
    }

    @Override
    public void atTurnStart() {
        // 每个回合开始时增加计数
        this.counter++;

        // 当计数达到 2 时
        if (this.counter >= TRIGGER_TURN) {
            this.flash();
            // 重置为 0，这样下次战斗开始时会接着上次的数字跑
            this.counter = 0;

            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            // 往抽牌堆洗入虚空
            addToBot(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, true));
        }
    }

    // 注意：这里删除了 atBattleStart 和 onVictory 对 counter 的赋值
    // 这样 counter 就会在整局游戏中持续累加/循环

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new VoidPrismRelic();
    }
}