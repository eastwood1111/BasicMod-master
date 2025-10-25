package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.CurrentStancePower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class GreatSwordStanceCard extends BaseCard {
    public static final String ID = makeID(GreatSwordStanceCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0
    );

    private static final int BASE_STR = 5;
    private static final int UPG_STR = 6;

    public GreatSwordStanceCard() {
        super(ID, info);
        this.baseMagicNumber = this.magicNumber = BASE_STR;
        this.rawDescription = "进入大剑架势，获得" + BASE_STR + "点力量。";
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CurrentStancePower stancePower = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);
        if (stancePower == null) {
            stancePower = new CurrentStancePower(p);
            p.addPower(stancePower);
        }

        // 切换架势并获得力量
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
            // 升级力量值
            upgradeMagicNumber(UPG_STR - BASE_STR);
            this.rawDescription = "升级后：进入大剑架势，获得" + UPG_STR + "点力量。抽1张牌。";
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GreatSwordStanceCard();
    }
}
