package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.InnerPotentialPower;
import basicmod.powers.MagicPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class InnerPotentialCard extends BaseCard {

    public static final String ID = makeID(InnerPotentialCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int STRENGTH = 3;
    private static final int INTELLECT = 3;
    private static final int LIFE_LOSS = 2;

    public InnerPotentialCard() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int lifeLoss = upgraded ? 1 : 2;

        // 获得力量
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 3), 3));

        // 获得智力
        addToBot(new ApplyPowerAction(p, p, new MagicPower(p, 3), 3));

        // 内在潜力每回合失血
        addToBot(new ApplyPowerAction(p, p, new InnerPotentialPower(p, lifeLoss), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new InnerPotentialCard();
    }
}
