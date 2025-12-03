package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction; // 引入攻击特效
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import basemod.ReflectionHacks; // 【重要】需要引入这个类来获取多段攻击次数

public class ReturnAttack extends BaseCard {
    public static final String ID = "basicmod:ReturnAttack";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2
    );

    public ReturnAttack() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            // 1. 获取单段基础伤害
            int intentDamage = m.getIntentBaseDmg();
            if (intentDamage < 0) intentDamage = 0;

            // 2. 【核心修复】通过反射检查是否为多段攻击，并计算总伤害
            // 读取 protected 字段 isMultiDmg
            boolean isMultiDmg = (boolean) ReflectionHacks.getPrivate(m, AbstractMonster.class, "isMultiDmg");

            if (isMultiDmg) {
                // 读取 protected 字段 intentMultiAmt (攻击段数)
                int multiAmt = (int) ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
                intentDamage *= multiAmt; // 总伤害 = 单段 * 次数
            }

            // 3. 设置这张卡的基础伤害为怪物的总伤害
            // 这样做的优点是：可以利用卡牌自身的 calculateCardDamage 方法来正确应用玩家的力量、易伤等效果
            this.baseDamage = intentDamage;
            this.calculateCardDamage(m);

            // 4.造成伤害
            // 注意：这里使用 this.damage，它是经过 calculateCardDamage 计算后的最终数值
            addToBot(new DamageAction(
                    m,
                    new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.SLASH_HEAVY // 建议添加一个打击特效
            ));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(1);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ReturnAttack();
    }
}