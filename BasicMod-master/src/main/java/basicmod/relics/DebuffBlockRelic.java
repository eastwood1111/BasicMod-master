package basicmod.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
// 必须导入这个接口
import com.evacipated.cardcrawl.mod.stslib.relics.OnApplyPowerRelic;

import static basicmod.BasicMod.makeID;

// 关键点：添加 implements OnApplyPowerRelic
public class DebuffBlockRelic extends BaseRelic implements OnApplyPowerRelic {
    public static final String ID = makeID("DebuffBlockRelic");

    public DebuffBlockRelic() {
        super(ID, "myrelic", RelicTier.RARE, LandingSound.CLINK);
    }

    /**
     * 来自 OnApplyPowerRelic 接口的方法
     * 返回值 true 表示允许施加该能力，false 表示阻止施加
     */
    @Override
    public boolean onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        // 判断：玩家施加给敌人，且是负面效果
        if (source == AbstractDungeon.player && target != AbstractDungeon.player && power.type == AbstractPower.PowerType.DEBUFF) {

            if (power.amount > 0) {
                this.flash();
                // 提示遗物激活
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                // 获得等同于层数的格挡
                addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, power.amount));
            }
        }
        return true; // 必须返回 true，否则负面状态加不上去
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DebuffBlockRelic();
    }
}