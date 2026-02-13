package basicmod.relics;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnApplyPowerRelic;

import static basicmod.BasicMod.makeID;

public class DebuffBlockRelic extends BaseRelic implements OnApplyPowerRelic {
    public static final String ID = makeID("DebuffBlockRelic");

    public DebuffBlockRelic() {
        super(ID, "myrelic", RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public boolean onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        // 基础判断：玩家给敌人施加负面效果
        if (source == AbstractDungeon.player && target != AbstractDungeon.player && power.type == AbstractPower.PowerType.DEBUFF) {

            // --- 修复开始 ---
            // 检查目标是否有 "Artifact" (人工制品)
            // 如果有，说明这次 Debuff 会被抵消，所以不触发遗物效果
            boolean hasArtifact = target.hasPower("Artifact");

            if (!hasArtifact && power.amount > 0) {
                this.flash();
                addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                addToBot(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, power.amount));
            }
            // --- 修复结束 ---
        }

        // 必须返回 true，我们只是在判断是否给格挡，而不是阻止 Debuff 本身的施加逻辑
        // 如果这里因为有 artifact 而返回 false，会导致人工制品层数不消耗（因为施加动作被你拦截了）
        return true;
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