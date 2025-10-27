package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.AttackWeakPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AttackWeakCard extends BaseCard {
    public static final String ID = makeID(AttackWeakCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );

    public AttackWeakCard() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 添加自定义 Power，控制打出攻击牌触发
        if (!p.hasPower(AttackWeakPower.POWER_ID)) {
            p.addPower(new AttackWeakPower(p, upgraded));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级后 Power 作用全体敌人
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new AttackWeakCard();
    }
}
