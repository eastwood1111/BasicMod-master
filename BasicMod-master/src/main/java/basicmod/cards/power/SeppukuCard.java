package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.SeppukuPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SeppukuCard extends BaseCard {
    public static final String ID = makeID(SeppukuCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            2
    );

    public SeppukuCard() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 给自己添加 SeppukuPower，如果已有就叠加
        if (p.getPower(SeppukuPower.POWER_ID) == null) {
            addToBot(new ApplyPowerAction(p, p, new SeppukuPower(p, 1)));
        } else {
            addToBot(new ApplyPowerAction(p, p, new SeppukuPower(p, 1)));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SeppukuCard();
    }
}
