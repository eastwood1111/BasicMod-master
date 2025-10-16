package basicmod.cards;

import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.StrengthBoostPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class StrengthBoostCard extends BaseCard {
    public static final String ID = makeID(StrengthBoostCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            1 // 费用
    );

    private static final int STRENGTH = 2;

    public StrengthBoostCard() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = upgraded ? 3 : STRENGTH;
        addToBot(new ApplyPowerAction(p, p, new StrengthBoostPower(p, amount)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StrengthBoostCard();
    }
}
