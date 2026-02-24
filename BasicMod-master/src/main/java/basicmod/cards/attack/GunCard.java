package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.StunPower; // 确保此类存在
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class GunCard extends BaseCard {
    public static final String ID = makeID(GunCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            1
    );

    // 定义数值常量
    private static final int DAMAGE_STRIKE = 8;
    private static final int DAMAGE_WEAK = 1;
    private static final int STUN_AMOUNT = 1;
    private static final int VULN_AMOUNT = 3;

    public GunCard() {
        super(ID, info);
        // 设置基础攻击力
        setDamage(DAMAGE_STRIKE);

        // 设置魔术数字（用于易伤层数），这样描述里的数字可以是蓝色的动态值
        setMagic(VULN_AMOUNT);

        this.exhaust = true; // 消耗
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 判断怪物是否处于攻击意图
        // 这里的逻辑是正确的，涵盖了原版所有的攻击意图
        boolean isAttacking = (m.intent == AbstractMonster.Intent.ATTACK ||
                m.intent == AbstractMonster.Intent.ATTACK_BUFF ||
                m.intent == AbstractMonster.Intent.ATTACK_DEBUFF ||
                m.intent == AbstractMonster.Intent.ATTACK_DEFEND);

        if (isAttacking) {
            // 效果1：攻击意图 -> 主伤害(8) + 击晕
            // 注意：使用 this.damage，这是经过计算后的最终伤害
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));

            // 施加击晕
            addToBot(new ApplyPowerAction(m, p, new StunPower(m, STUN_AMOUNT), STUN_AMOUNT));
        } else {
            // 效果2：非攻击意图 -> 弱伤害(1)
            // 这里直接传数字会导致它享受力量加成。
            // 这里我们保持原样（受力量影响），但使用 BLUNT_LIGHT 轻音效
            addToBot(new DamageAction(m, new DamageInfo(p, DAMAGE_WEAK, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }

        // 升级效果：额外施加易伤
        // 使用 magicNumber 代替硬编码的 constants
        if (upgraded) {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, this.magicNumber, false), this.magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 如果不需要修改数值，只是添加易伤效果，确保 cardStrings.json 里有正确的描述
            // upgradeMagicNumber(1); // 如果你想让易伤层数也能升级，可以加这一行

            // 强制更新描述
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}