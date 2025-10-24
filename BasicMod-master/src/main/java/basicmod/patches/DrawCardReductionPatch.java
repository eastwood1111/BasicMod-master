package basicmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import basicmod.powers.CustomDrawReductionPower;

@SpirePatch(clz = DrawCardAction.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {int.class, boolean.class})
public class DrawCardReductionPatch {

    @SpirePostfixPatch
    public static void reduceDraw(DrawCardAction __instance, int numCards, boolean endTurnDraw) {
        AbstractPlayer player = AbstractDungeon.player;
        if (player.hasPower(CustomDrawReductionPower.POWER_ID)) {
            CustomDrawReductionPower power =
                    (CustomDrawReductionPower) player.getPower(CustomDrawReductionPower.POWER_ID);

            // 限制减少数量不会超过抽牌总数
            int reduce = Math.min(power.amount, __instance.amount - 1); // 保证至少抽 1 张
            __instance.amount -= reduce;
        }
    }
}
