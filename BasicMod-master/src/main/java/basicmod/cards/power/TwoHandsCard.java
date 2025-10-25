package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.CurrentStancePower;
import basicmod.powers.TwoHandsPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TwoHandsCard extends BaseCard {
    public static final String ID = makeID(TwoHandsCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            2
    );

    public TwoHandsCard() {
        super(ID, info);
        this.rawDescription = "启用双手各持：可以同时持有两个架势";
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 添加双手各持状态
        if (!p.hasPower(TwoHandsPower.POWER_ID)) {
            p.addPower(new TwoHandsPower(p));
        }

        CurrentStancePower stancePower = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);
        if (stancePower == null) {
            stancePower = new CurrentStancePower(p);
            p.addPower(stancePower);
        }

        // 双手各持不需要直接抽牌，这张卡主要是启用双架势
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = "升级后效果不变：启用双手各持，可同时持有两个架势，每次进入架势抽1张牌。";
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TwoHandsCard();
    }
}
