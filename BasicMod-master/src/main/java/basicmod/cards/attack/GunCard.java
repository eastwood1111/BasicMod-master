package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.StunPower; // 确保导入了你的击晕能力
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
            CardType.ATTACK,      // 修改为攻击牌
            CardRarity.RARE,
            CardTarget.ENEMY,     // 修改为指向敌人
            1                     // 费用 1
    );

    // 定义数值常量
    private static final int DAMAGE_STRIKE = 8;
    private static final int DAMAGE_WEAK = 1;
    private static final int STUN_AMOUNT = 1;
    private static final int VULN_AMOUNT = 3;

    public GunCard() {
        super(ID, info);
        setDamage(DAMAGE_STRIKE); // 设置基础伤害为8
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 判断怪物是否处于攻击意图
        boolean isAttacking = (m.intent == AbstractMonster.Intent.ATTACK ||
                m.intent == AbstractMonster.Intent.ATTACK_BUFF ||
                m.intent == AbstractMonster.Intent.ATTACK_DEBUFF ||
                m.intent == AbstractMonster.Intent.ATTACK_DEFEND);

        if (isAttacking) {
            // 效果1：若是攻击意图，造成8点伤害并施加击晕
            addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            addToBot(new ApplyPowerAction(m, p, new StunPower(m, STUN_AMOUNT), STUN_AMOUNT));
        } else {
            // 效果2：若不是攻击意图，造成1点伤害
            addToBot(new DamageAction(m, new DamageInfo(p, DAMAGE_WEAK, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }

        // 升级效果：额外施加3层易伤
        if (upgraded) {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, VULN_AMOUNT, false), VULN_AMOUNT));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 这里不再修改费用（保持1费），而是标记升级状态以触发 use 中的易伤逻辑
            // 如果你想修改描述，可以在这里设置
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}