package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.StanceDefendPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

public class StanceDefendCard extends BaseCard {
    public static final String ID = makeID(StanceDefendCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            1 // 费用
    );

    private static final int BASE_STACK = 3;
    private static final int UPG_STACK = 4;

    public StanceDefendCard() {
        super(ID, info);
        this.name = NAME;
        this.baseMagicNumber = this.magicNumber = BASE_STACK;
        this.rawDescription = DESCRIPTION.replace("!STACK!", String.valueOf(BASE_STACK));
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 每次使用都叠加相同层数
        addToBot(new ApplyPowerAction(p, p, new StanceDefendPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.baseMagicNumber = this.magicNumber = UPG_STACK;
            this.rawDescription = DESCRIPTION.replace("!STACK!", String.valueOf(UPG_STACK));
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StanceDefendCard();
    }
}
