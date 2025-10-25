package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.CurrentStancePower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import basicmod.BasicMod;

public class GreatSwordStanceCard extends BaseCard {
    public static final String ID = makeID(GreatSwordStanceCard.class.getSimpleName());
    private static final CardStrings cardStrings = com.megacrit.cardcrawl.core.CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0
    );

    private static final int BASE_STR = 6;
    private static final int UPG_STR = 6;

    public GreatSwordStanceCard() {
        super(ID, info);
        this.name = NAME;
        this.baseMagicNumber = this.magicNumber = BASE_STR;
        this.rawDescription = DESCRIPTION.replace("!STR!", String.valueOf(BASE_STR));
        this.tags.add(basicmod.Enums.STANCE);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CurrentStancePower stancePower = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);
        if (stancePower == null) {
            stancePower = new CurrentStancePower(p);
            p.addPower(stancePower);
        }

        stancePower.switchStance(CurrentStancePower.Stance.GREAT_SWORD, this.magicNumber);

        // 升级后才抽1张牌
        if (upgraded) {
            addToBot(new DrawCardAction(p, 1));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_STR - BASE_STR);
            this.rawDescription = DESCRIPTION.replace("!STR!", String.valueOf(UPG_STR)) + " 抽1张牌。";
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GreatSwordStanceCard();
    }
}
