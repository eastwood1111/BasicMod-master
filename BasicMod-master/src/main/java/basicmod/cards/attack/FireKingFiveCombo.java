package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FireKingFiveCombo extends BaseCard {

    public static final String ID = makeID(FireKingFiveCombo.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ALL_ENEMY, // 建议改为 ALL_ENEMY，因为是随机攻击所有敌人
            2
    );

    private static final int DAMAGE = 3;
    private static final int UPGRADE_DAMAGE = 1; // 升级增加的数值
    private static final int HIT_COUNT = 5;

    public FireKingFiveCombo() {
        super(ID, info);
        // 如果 BaseCard 里没有处理 strings，这里保持原样，否则建议用 initializeDescription
        // this.name = cardStrings.NAME;
        // this.rawDescription = cardStrings.DESCRIPTION;

        this.baseDamage = DAMAGE;
        // 不需要手动 this.damage = this.baseDamage，父类构造通常会处理，或者在 applyPowers 时自动处理
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 循环 5 次，每次加入一个“攻击随机敌人”的动作
        for (int i = 0; i < HIT_COUNT; i++) {
            this.addToBot(new AttackDamageRandomEnemyAction(
                    this,
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
            ));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE); // 标准写法：增加伤害而不是直接赋值
            // this.rawDescription = cardStrings.UPGRADE_DESCRIPTION; // 如果描述没变不需要这行
            initializeDescription();
        }
    }

    @Override
    public BaseCard makeCopy() {
        return new FireKingFiveCombo();
    }
}