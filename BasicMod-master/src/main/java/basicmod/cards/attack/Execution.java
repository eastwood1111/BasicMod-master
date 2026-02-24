package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.cards.skill.AdorationRing;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Execution extends BaseCard {
    public static final String ID = makeID(Execution.class.getSimpleName());
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            1 // 初始费用
    );

    private static final float BASE_PERCENT = 0.08f;  // 8%
    private static final float UPG_PERCENT = 0.10f;   // 升级 10%
    private static final float STUN_MULTIPLIER = 2.0f; // 击晕加倍

    private float percentDamage;

    public Execution() {
        super(ID, info);
        this.percentDamage = BASE_PERCENT;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            // 先计算基础百分比伤害
            int baseDmg = Math.round(m.maxHealth * percentDamage);

            // 击晕加倍
            if (m.hasPower("StunPower")) {
                baseDmg = Math.round(baseDmg * STUN_MULTIPLIER);
            }

            // 创建临时 DamageInfo 对象
            DamageInfo dmgInfo = new DamageInfo(p, baseDmg, DamageInfo.DamageType.NORMAL);

            // 让游戏自动应用力量、易伤等加成
            dmgInfo.applyPowers(p, m);

            // 执行伤害动作
            addToBot(new DamageAction(m, dmgInfo));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.percentDamage = UPG_PERCENT; // 升级基础百分比
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Execution();
    }
}
