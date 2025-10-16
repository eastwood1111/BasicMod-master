package basicmod.cards;

import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SimpleStrike extends BaseCard {
    public static final String ID = makeID(SimpleStrike.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON, // 白色普通卡
            CardTarget.ENEMY,
            1 // 费用
    );

    private static final int DAMAGE = 10;

    public SimpleStrike() {
        super(ID, info);
        setDamage(DAMAGE, 0); // 普通攻击没有升级增加
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
        ));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription(); // 白卡可不增加伤害
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SimpleStrike();
    }
}

