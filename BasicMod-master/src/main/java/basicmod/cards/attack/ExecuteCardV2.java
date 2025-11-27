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

public class ExecuteCardV2 extends BaseCard {

    public static final String ID = makeID(ExecuteCardV2.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            3 // 消耗 3
    );

    private static final int DAMAGE = 20;      // 初始伤害
    private static final int UPG_DAMAGE = 25;  // 升级伤害
    private static final int TRIGGER_HP = 20;  // 血量阈值（固定值）
    private static final int UPG_TRIGGER_HP = 25;

    public ExecuteCardV2() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();

        this.baseDamage = DAMAGE;
        this.damage = this.baseDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 先造成固定伤害
        this.baseDamage = upgraded ? UPG_DAMAGE : DAMAGE;
        this.calculateCardDamage(m);

        addToBot(new DamageAction(
                m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_HEAVY
        ));

        // 再判断敌方血量是否低于固定阈值，如果是，则再次造成同样伤害
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int triggerHp = upgraded ? UPG_TRIGGER_HP : TRIGGER_HP;
                if (m.currentHealth > 0 && m.currentHealth <= triggerHp) {
                    // 再次造成固定伤害
                    addToBot(new DamageAction(
                            m,
                            new DamageInfo(p, ExecuteCardV2.this.baseDamage, ExecuteCardV2.this.damageTypeForTurn),
                            AbstractGameAction.AttackEffect.SLASH_HEAVY
                    ));
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.baseDamage = UPG_DAMAGE;
            this.damage = this.baseDamage;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ExecuteCardV2();
    }
}
