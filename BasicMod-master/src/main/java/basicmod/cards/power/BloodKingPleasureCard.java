package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.cards.skill.AdorationRing;
import basicmod.charater.MyCharacter;
import basicmod.powers.BloodKingPleasurePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BloodKingPleasureCard extends BaseCard {

    public static final String ID = makeID(BloodKingPleasureCard.class.getSimpleName());
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    public BloodKingPleasureCard() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = 1; // 力量获得量
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1); // 变为 2 力量
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new BloodKingPleasurePower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BloodKingPleasureCard();
    }
}
