package basicmod.potions;

import basicmod.cards.StanceChoice;
import basicmod.charater.MyCharacter;
import basicmod.powers.CurrentStancePower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PotionStrings;
import java.util.ArrayList;

import static basicmod.BasicMod.makeID;

public class StylePotion extends BasePotion {
    public static final String POTION_ID = makeID("StylePotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public StylePotion() {
        super(POTION_ID, 1, PotionRarity.RARE, PotionSize.BOTTLE, Color.PURPLE.cpy(), Color.GOLD.cpy(), null);
        this.playerClass = MyCharacter.Meta.soulfive;
        this.isThrown = false;
    }

    @Override
    public void use(AbstractCreature target) {
        // 获取效力：普通=1，神圣树皮=2
        int effectCount = getPotency();

        // 循环执行。如果有神圣树皮，第一个选择做完后，会自动弹出第二个选择
        for (int i = 0; i < effectCount; i++) {
            ArrayList<AbstractCard> choices = new ArrayList<>();

            // 注意：因为是选两次，所以这里的数值保持基础值（6点）
            int ironAmt = 6;
            int swordAmt = 2;

            choices.add(new StanceChoice(
                    "大剑",
                    "进入大剑架势。 NL 获得 " + swordAmt + " 点力量。",
                    "basicmod/images/ui/great_sword.png",
                    CurrentStancePower.Stance.GREAT_SWORD,
                    swordAmt
            ));

            choices.add(new StanceChoice(
                    "祷告/魔法",
                    "进入祷告/魔法架势。",
                    "basicmod/images/ui/magic.png",
                    CurrentStancePower.Stance.MAGIC,
                    0
            ));

            choices.add(new StanceChoice(
                    "韧狗",
                    "进入韧狗架势。 NL 获得 " + ironAmt + " 点金属化。",
                    "basicmod/images/ui/iron.png",
                    CurrentStancePower.Stance.IRON,
                    ironAmt
            ));

            // 将动作加入队列
            addToBot(new ChooseOneAction(choices));
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 1;
    }

    @Override
    public String getDescription() {
        // 如果有神圣树皮，描述会显示“进行 2 次选择”
        if (getPotency() > 1) {
            return potionStrings.DESCRIPTIONS[1]; // 对应 JSON 中第二条描述
        }
        return potionStrings.DESCRIPTIONS[0];
    }
}