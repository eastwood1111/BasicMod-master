package basicmod.cards;

import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.EndOfTurnAOEPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class EndOfTurnAOECard extends BaseCard {
    public static final String ID = makeID(EndOfTurnAOECard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            2 // 初始费用
    );

    private static final int DAMAGE = 10;
    private static final int TURNS = 3;

    public EndOfTurnAOECard() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int turns = upgraded ? 4 : TURNS;
        addToBot(new ApplyPowerAction(p, p, new EndOfTurnAOEPower(p, DAMAGE, turns)));
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
        return new EndOfTurnAOECard();
    }
}
