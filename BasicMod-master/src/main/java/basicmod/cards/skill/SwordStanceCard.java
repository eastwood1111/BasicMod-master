package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.CurrentStancePower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SwordStanceCard extends BaseCard {
    public static final String ID = makeID(SwordStanceCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.SELF,
            0
    );

    private static final int BASE_STR = 3;
    private static final int UPG_STR = 4;

    public SwordStanceCard() {
        super(ID, info);
        this.baseMagicNumber = this.magicNumber = BASE_STR;
        this.rawDescription = "进入剑架势，获得" + BASE_STR + "点力量。抽1张牌。";
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

        // 基础卡自带抽1张，升级卡也抽1张（和基础一致）
        addToBot(new DrawCardAction(p, 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_STR - BASE_STR);
            this.rawDescription = "升级后：进入剑架势，获得" + UPG_STR + "点力量。抽1张牌。";
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SwordStanceCard();
    }
}
