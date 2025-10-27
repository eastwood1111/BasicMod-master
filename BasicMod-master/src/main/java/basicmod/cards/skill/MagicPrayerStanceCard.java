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
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class MagicPrayerStanceCard extends BaseCard {
    public static final String ID = makeID(MagicPrayerStanceCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            1
    );

    private static final int MAGIC_BOOST = 1;
    private static final int DRAW = 1; // 基础抽牌
    private static final int DRAW_UPG = 2; // 升级后抽牌

    private int drawAmount;

    public MagicPrayerStanceCard() {
        super(ID, info);
        this.name = NAME;
        this.rawDescription = DESCRIPTION;
        this.drawAmount = DRAW; // 初始化为基础抽牌
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CurrentStancePower stancePower = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);
        if (stancePower == null) {
            stancePower = new CurrentStancePower(p);
            p.addPower(stancePower);
        }

        stancePower.switchStance(CurrentStancePower.Stance.MAGIC, MAGIC_BOOST);

        addToBot(new DrawCardAction(p, drawAmount));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.drawAmount = DRAW_UPG; // 升级后抽牌变2张
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new MagicPrayerStanceCard();
    }
}
