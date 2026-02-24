package basicmod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PotionStrings;
import basicmod.charater.MyCharacter;

public class EmberPotion extends BasePotion {
    // 采用硬编码 ID，确保即使 BasicMod 报错，这里也能编译通过
    public static final String POTION_ID = "soul-five:EmberPotion";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public EmberPotion() {
        // ID, 效力(20), 稀有度, 形状, 液体颜色, 混合液颜色, 斑点颜色
        super(POTION_ID, 20, PotionRarity.RARE, PotionSize.BOTTLE, Color.ORANGE.cpy(), Color.RED.cpy(), null);

        // 核心修改：使用你在 MyCharacter 中定义的最新变量名 SOUL_FIVE_CLASS
        this.playerClass = MyCharacter.Meta.SOUL_FIVE_CLASS;

        this.isThrown = false;
    }

    @Override
    public void use(AbstractCreature target) {
        // 回复最大生命值的百分比
        float percent = this.getPotency() / 100.0F;
        int healAmount = (int)(AbstractDungeon.player.maxHealth * percent);
        if (healAmount > 0) {
            AbstractDungeon.player.heal(healAmount);
        }

        // 增加最大生命上限 (受神圣树皮影响时 potency 会翻倍)
        // 默认 20 效力加 3 点，翻倍后加 6 点
        int maxHpAmount = (this.getPotency() <= 20) ? 3 : 6;
        AbstractDungeon.player.increaseMaxHp(maxHpAmount, true);
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 20;
    }

    @Override
    public String getDescription() {
        // 确保你的 potions.json 里有足够长度的 DESCRIPTIONS 数组
        int maxHpAmount = (this.getPotency() <= 20) ? 3 : 6;
        if (potionStrings == null) return "Description missing.";
        return potionStrings.DESCRIPTIONS[0] + this.getPotency() + potionStrings.DESCRIPTIONS[1] + maxHpAmount + potionStrings.DESCRIPTIONS[2];
    }
}