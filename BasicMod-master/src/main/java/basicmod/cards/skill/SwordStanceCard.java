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

public class SwordStanceCard extends BaseCard {
    public static final String ID = makeID(SwordStanceCard.class.getSimpleName());
    private static final CardStrings cardStrings = com.megacrit.cardcrawl.core.CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            1
    );

    private static final int BASE_STR = 3;
    private static final int UPG_STR = 4;

    public SwordStanceCard() {
        super(ID, info);
        this.name = NAME;
        this.baseMagicNumber = this.magicNumber = BASE_STR;
        this.rawDescription = DESCRIPTION.replace("!STR!", String.valueOf(BASE_STR));

        // 保留手牌
        this.selfRetain = true;
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

        stancePower.switchStance(CurrentStancePower.Stance.SWORD, this.magicNumber);

        // 每次进入架势都抽1张牌
        addToBot(new DrawCardAction(p, 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_STR - BASE_STR);
            this.rawDescription = DESCRIPTION.replace("!STR!", String.valueOf(UPG_STR)); // 升级后的占位符替换
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SwordStanceCard();
    }
}
