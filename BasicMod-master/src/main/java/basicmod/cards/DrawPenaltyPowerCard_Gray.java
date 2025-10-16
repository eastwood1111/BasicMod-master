package basicmod.cards;

import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.PowerPenaltyDrawPower_Gray;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class DrawPenaltyPowerCard_Gray extends BaseCard {
    public static final String ID = makeID(DrawPenaltyPowerCard_Gray.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.BASIC,
            CardTarget.SELF,
            1
    );

    private static final int STR = 3;
    private static final int DEX = 3;

    public DrawPenaltyPowerCard_Gray() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int str = upgraded ? 4 : STR;
        int dex = upgraded ? 4 : DEX;
        addToBot(new ApplyPowerAction(p, p, new PowerPenaltyDrawPower_Gray(p, str, dex)));
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
        return new DrawPenaltyPowerCard_Gray();
    }
}

