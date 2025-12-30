package basicmod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PotionStrings;
import basicmod.charater.MyCharacter; // 确保路径正确

import static basicmod.BasicMod.makeID;

public class EmberPotion extends BasePotion {
    public static final String POTION_ID = makeID("EmberPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public EmberPotion() {
        // 参数对应 BasePotion 的构造器:
        // ID, 效力(20), 稀有度, 形状, 液体颜色, 混合液颜色, 斑点颜色
        super(POTION_ID, 20, PotionRarity.RARE, PotionSize.BOTTLE, Color.ORANGE.cpy(), Color.RED.cpy(), null);

        // 关键点：使用你在 MyCharacter.java 中定义的 Meta.soulfive
        this.playerClass = MyCharacter.Meta.soulfive;

        this.isThrown = false;
    }

    @Override
    public void use(AbstractCreature target) {
        // 回复最大生命值的 20% (potency 默认为 20)
        int healAmount = (int)(AbstractDungeon.player.maxHealth * (this.getPotency() / 100.0F));
        if (healAmount > 0) {
            AbstractDungeon.player.heal(healAmount);
        }

        // 增加 3 点最大生命上限 (受神圣树皮影响时 potency 变大，增加量也翻倍)
        int maxHpAmount = (this.getPotency() <= 20) ? 3 : 6;
        AbstractDungeon.player.increaseMaxHp(maxHpAmount, true);
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 20;
    }

    @Override
    public String getDescription() {
        // 这里的逻辑需要匹配你的 JSON 文本数组长度
        int maxHpAmount = (this.getPotency() <= 20) ? 3 : 6;
        return potionStrings.DESCRIPTIONS[0] + this.getPotency() + potionStrings.DESCRIPTIONS[1] + maxHpAmount + potionStrings.DESCRIPTIONS[2];
    }
}