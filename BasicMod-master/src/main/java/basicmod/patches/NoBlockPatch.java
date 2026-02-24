package basicmod.patches;

import basicmod.powers.NoBlockPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "addBlock"
)
public class NoBlockPatch {

    @SpirePrefixPatch
    public static SpireReturn<Void> Prefix(AbstractCreature __instance, int blockAmount) {
        // 1. 首先检查是否有该能力
        if (__instance.hasPower(NoBlockPower.POWER_ID)) {

            // 2. 检查当前的 Action 是不是 GainBlockAction
            // 大多数直接通过卡牌获得的格挡都属于这个 Action
            if (AbstractDungeon.actionManager.currentAction instanceof GainBlockAction) {
                return SpireReturn.Return(null);
            }

            /* * 3. (可选) 进阶：拦截像“猛击(Wallop)”这种在 DamageAction 中直接调用 addBlock 的卡牌
             * 如果你需要拦截这类特殊的攻击卡格挡，可以加上：
             * if (AbstractDungeon.actionManager.currentAction instanceof DamageAction) { ... }
             */
        }

        return SpireReturn.Continue();
    }
}