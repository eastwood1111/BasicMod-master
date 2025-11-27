package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;

public class WarmFireCard extends BaseCard {

    public static final String ID = makeID(WarmFireCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1 // 消耗 1
    );

    private static final int REGEN = 3;
    private static final int UPG_REGEN = 1;

    public WarmFireCard() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();

        this.baseMagicNumber = REGEN;
        this.magicNumber = this.baseMagicNumber;

        this.exhaust = true; // 消耗
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(
                p,
                p,
                new RegenPower(p, this.magicNumber),
                this.magicNumber
        ));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_REGEN); // 升级增加 1 层再生
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WarmFireCard();
    }
}
