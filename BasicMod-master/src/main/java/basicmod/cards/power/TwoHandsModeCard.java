package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.localization.CardStrings;
import basicmod.BasicMod;

public class TwoHandsModeCard extends BaseCard {
    public static final String ID = makeID(TwoHandsModeCard.class.getSimpleName());
    private static final CardStrings cardStrings = com.megacrit.cardcrawl.core.CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1 // 能量费用
    );

    private static final int STR_AMOUNT = 3;

    public TwoHandsModeCard() {
        super(ID, info);
        this.name = NAME;
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.addPower(new StrengthPower(p, STR_AMOUNT));
    }

    @Override
    public void upgrade() {
        // 不可升级
    }

    @Override
    public AbstractCard makeCopy() {
        return new TwoHandsModeCard();
    }
}
