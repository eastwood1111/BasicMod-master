package basicmod.potions;

import basemod.abstracts.CustomPotion;
import basicmod.BasicMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static basicmod.BasicMod.makeID;

public class MyPotion extends CustomPotion {

    public static final String POTION_ID = makeID("MyPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    // 颜色
    private static final Color LIQUID_COLOR = CardHelper.getColor(255, 0, 255);   // 药水液体颜色
    private static final Color HYBRID_COLOR = CardHelper.getColor(255, 0, 255);  // 混合颜色
    private static final Color SPOTS_COLOR = CardHelper.getColor(255, 0, 255);   // 气泡颜色

    public MyPotion() {
        super(
                potionStrings.NAME,          // 名称
                POTION_ID,                   // ID
                PotionRarity.UNCOMMON,       // 稀有度
                PotionSize.BOTTLE,           // 大小
                PotionColor.FIRE);           // 基础颜色（可用CustomColor或FIRE等）

        // 设置自定义颜色
        this.liquidColor = LIQUID_COLOR;
        this.hybridColor = HYBRID_COLOR;
        this.spotsColor = SPOTS_COLOR;

        this.isThrown = true;      // 是否投掷型
        this.targetRequired = true; // 是否需要目标
    }

    @Override
    public void use(AbstractCreature target) {
        // 使用药水效果，例如增加力量
        addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new StrengthPower(target, 3), 3));
    }

    @Override
    public int getPotency(int i) {
        return 0;
    }

    @Override
    public CustomPotion makeCopy() {
        return new MyPotion();
    }
}
