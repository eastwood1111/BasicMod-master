package basicmod.patches;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static basicmod.BasicMod.modID;

@SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
public class PrefixKeywordTooltipPatch {

    private static final Pattern P = Pattern.compile(
            Pattern.quote(modID.toLowerCase()) + ":([\\p{L}_]+)",
            Pattern.CASE_INSENSITIVE
    );

    @SpirePostfixPatch
    public static void postfix(AbstractCard __instance) {
        if (__instance == null || __instance.rawDescription == null) return;

        Matcher m = P.matcher(__instance.rawDescription);
        while (m.find()) {
            String token = m.group(1);
            if (token == null || token.isEmpty()) continue;

            String key = modID.toLowerCase() + ":" + token.toLowerCase();

            String unique = BaseMod.getKeywordUnique(key);
            if (unique == null) unique = key;

            String desc = GameDictionary.keywords.get(unique);
            if (desc == null) continue; // 没注册成功就跳过

            String proper = BaseMod.getKeywordProper(unique);
            if (proper == null || proper.isEmpty()) proper = token.replace('_', ' ');

            // 1) keywords（通常是 public 的）
            if (!__instance.keywords.contains(unique)) {
                __instance.keywords.add(unique);
            }

            // 2) tips（很多版本是 private，需要反射拿出来）
            ArrayList<PowerTip> tips = ReflectionHacks.getPrivate(__instance, AbstractCard.class, "tips");
            if (tips == null) {
                tips = new ArrayList<>();
                ReflectionHacks.setPrivate(__instance, AbstractCard.class, "tips", tips);
            }

            boolean exists = false;
            for (PowerTip t : tips) {
                if (t != null && proper.equals(t.header)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                tips.add(new PowerTip(proper, desc));
            }
        }
    }
}