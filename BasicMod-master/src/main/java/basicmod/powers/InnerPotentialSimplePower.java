package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;

public class InnerPotentialSimplePower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:InnerPotentialSimplePower";
    public static final String NAME = "内在潜力";
    public static final String[] DESCRIPTIONS = new String[]{"你造成的伤害翻倍，每打出一张牌失去 #b 生命"};

    public int loseHpPerCard; // 每打出一张牌扣血

    public InnerPotentialSimplePower(AbstractCreature owner, int loseHp) {
        this.owner = owner;
        this.loseHpPerCard = loseHp;
        this.name = NAME;
        this.ID = POWER_ID;
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

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        // 每打出一张牌扣血
        this.addToBot(new LoseHPAction(owner, owner, loseHpPerCard));
    }

    /** 攻击牌伤害翻倍 */
    public int atDamageGive(int damage, AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            return damage * 2;
        }
        return damage;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + loseHpPerCard + "点。";
    }

    @Override
    public AbstractPower makeCopy() {
        return new InnerPotentialSimplePower(owner, loseHpPerCard);
    }
}
