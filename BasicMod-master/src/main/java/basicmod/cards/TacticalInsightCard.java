package basicmod.cards;

import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.TacticalInsightPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class TacticalInsightCard extends BaseCard {
    public static final String ID = makeID(TacticalInsightCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            2 // 费用
    );

    public TacticalInsightCard() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TacticalInsightPower(p, upgraded)));
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
        return new TacticalInsightCard();
    }
}

