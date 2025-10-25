package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import basicmod.Enums;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class StanceDrawPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:StanceDrawPower";
    public static final String NAME = "架势抽牌";
    public static final String[] DESCRIPTIONS = {
            "每次打出架势牌时，抽 #b 张牌。"
    };

    private final int cardsPerLayer = 1; // 每层触发抽牌数量固定 1

    public StanceDrawPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 1; // 初始层数为1
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // 图标
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        // 只对 Skill 类型架势牌生效
        if (Enums.isStanceCard(card) && card.type == AbstractCard.CardType.SKILL) {
            // 按 Power 层数抽牌
            addToBot(new DrawCardAction(owner, cardsPerLayer * this.amount));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount; // 每张 StanceTriggerCard 叠加一层
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("#b", String.valueOf(cardsPerLayer * this.amount));
        if (this.amount > 1) {
            this.description += " （已叠加 " + this.amount + " 层）";
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new StanceDrawPower(owner);
    }
}
