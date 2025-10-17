package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Guiyi extends BaseCard {
    public static final String ID = makeID(Guiyi.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.BASIC,
            CardTarget.ENEMY,
            2 // 能量消耗
    );

    private static final int MULTIPLIER = 3;
    private static final int UPG_MULTIPLIER = 5;

    public Guiyi() {
        super(ID, info);

        baseDamage = 0; // 实际伤害在 use 时计算
        this.magicNumber = this.baseMagicNumber = MULTIPLIER; // 保存倍数
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int handCount = p.hand.size(); // 当前手牌数量
        int damageAmount = handCount * this.magicNumber;

        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damageAmount, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_HEAVY
        ));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_MULTIPLIER - MULTIPLIER);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Guiyi();
    }
}

