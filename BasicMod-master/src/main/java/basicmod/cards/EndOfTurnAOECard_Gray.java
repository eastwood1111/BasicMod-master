package basicmod.cards;

import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.EndOfTurnAOEPower_Gray;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class EndOfTurnAOECard_Gray extends BaseCard {
    public static final String ID = makeID(EndOfTurnAOECard_Gray.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            2 // 初始费用
    );

    private static final int DAMAGE = 10;
    private static final int TURNS = 3;

    public EndOfTurnAOECard_Gray() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int turns = upgraded ? 4 : TURNS;
        addToBot(new ApplyPowerAction(p, p, new EndOfTurnAOEPower_Gray(p, DAMAGE, turns)));
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
        return new EndOfTurnAOECard_Gray();
    }
}
