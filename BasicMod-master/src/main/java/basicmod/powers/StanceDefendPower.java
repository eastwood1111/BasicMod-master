package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import basicmod.Enums;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class StanceDefendPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:StanceDefendPower";
    public static final String NAME = "架势防御";

    public StanceDefendPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount; // 每层提供1点格挡
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    // 每次打出架势牌时触发格挡
    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        if (Enums.isStanceCard(card) && card.type == AbstractCard.CardType.SKILL) {
            addToBot(new GainBlockAction(owner, owner, this.amount));
        }
    }

    // Power叠加逻辑
    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0f;
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = "每次打出架势牌时，获得 #b" + this.amount + " 点格挡。";
    }

    @Override
    public AbstractPower makeCopy() {
        return new StanceDefendPower(owner, amount);
    }
}
