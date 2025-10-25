package basicmod.cards.skill;

import basicmod.Enums;
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

public class ShieldStanceCard extends BaseCard {
    public static final String ID = makeID(ShieldStanceCard.class.getSimpleName());
    private static final CardStrings cardStrings = com.megacrit.cardcrawl.core.CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.SELF,
            0
    );

    private static final int BASE_DEX = 3;
    private static final int UPG_DEX = 4;

    public ShieldStanceCard() {
        super(ID, info);
        this.name = NAME;
        this.baseMagicNumber = this.magicNumber = BASE_DEX;
        this.rawDescription = DESCRIPTION.replace("!DEX!", String.valueOf(BASE_DEX));

        // 保留手牌
        this.selfRetain = true;
        // 添加自定义 Tag，标记为架势牌
        this.tags.add(Enums.STANCE);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CurrentStancePower stancePower = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);
        if (stancePower == null) {
            stancePower = new CurrentStancePower(p);
            p.addPower(stancePower);
        }

        stancePower.switchStance(CurrentStancePower.Stance.SHIELD, this.magicNumber);


        addToBot(new DrawCardAction(p, 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_DEX - BASE_DEX);
            this.rawDescription = DESCRIPTION.replace("!DEX!", String.valueOf(UPG_DEX));
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShieldStanceCard();
    }
}
