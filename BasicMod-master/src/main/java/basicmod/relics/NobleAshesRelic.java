package basicmod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import static basicmod.BasicMod.makeID;

public class NobleAshesRelic extends BaseRelic {
    public static final String ID = makeID("NobleAshesRelic");
    private static final float MULTIPLIER = 1.25F;

    public NobleAshesRelic() {
        super(ID, "myrelic", RelicTier.SHOP, LandingSound.MAGICAL);
    }

    /**
     * 修正后的方法名：onPlayerHeal
     * 这是 AbstractRelic 中定义的正确钩子
     */
    @Override
    public int onPlayerHeal(int healAmount) {
        // 计算增加后的数值
        int increasedHeal = (int)(healAmount * MULTIPLIER);

        // 如果增加了回复量，触发遗物闪烁
        if (increasedHeal > healAmount) {
            this.flash();
        }

        return increasedHeal;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new NobleAshesRelic();
    }
}