package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class GreedMarkCard extends BaseCard {

    public static final String ID = "basicmod:GreedMarkCard";
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );

    private int hpLoss; // 失去生命

    public GreedMarkCard() {
        super(ID, info);
        this.magicNumber = this.baseMagicNumber = 15; // 获得金币
        this.hpLoss = 4;                              // 失去生命
        this.name = cardStrings.NAME;
        this.exhaust = true;
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 失去生命
        addToBot(new LoseHPAction(p, p, this.hpLoss));
        // 获得金币
        addToBot(new GainGoldAction(this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.magicNumber = 20;  // 升级后获得金币
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GreedMarkCard();
    }
}
