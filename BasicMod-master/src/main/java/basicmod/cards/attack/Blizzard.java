package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Blizzard extends BaseCard {

    public static final String ID = makeID(Blizzard.class.getSimpleName());
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY,
            1
    );

    private static final int DAMAGE = 3;
    private static final int ATTACK_TIMES = 3;
    private static final int UPG_ATTACK_TIMES = 4;

    public Blizzard() {
        super(ID, info);
        setDamage(DAMAGE);
        exhaust = true; // 使用后消耗
        isMultiDamage = true; // 对所有敌人
        magicNumber = baseMagicNumber = ATTACK_TIMES;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对所有敌人多次造成伤害
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new DamageAllEnemiesAction(
                    p,
                    multiDamage,
                    DamageInfo.DamageType.NORMAL,
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
            ));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_ATTACK_TIMES - ATTACK_TIMES); // 攻击次数从3提升到4
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Blizzard();
    }
}
