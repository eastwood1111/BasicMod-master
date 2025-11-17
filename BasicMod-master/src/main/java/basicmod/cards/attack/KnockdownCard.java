package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.CurrentStancePower;
import basicmod.util.CardStats;
import basicmod.powers.StunPower;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class KnockdownCard extends BaseCard {

    public static final String ID = makeID(KnockdownCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2 // 初始费用
    );

    private static final int DAMAGE = 15;
    private static final int UPGRADE_DAMAGE = 5; // 升级 +5 → 20
    private static final int STUN_AMOUNT = 1;

    public KnockdownCard() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
        setDamage(DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        // 造成伤害
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_HEAVY
        ));

        // 获取架势Power
        CurrentStancePower stancePower =
                (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);

        // 判断是否处于大剑架势
        if (stancePower != null &&
                stancePower.getCurrentStance() == CurrentStancePower.Stance.GREAT_SWORD) {

            addToBot(new ApplyPowerAction(
                    m,
                    p,
                    new StunPower(m, 1),
                    1
            ));
        }
    }



    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            upgradeDamage(UPGRADE_DAMAGE); // 伤害 15 → 20
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new KnockdownCard();
    }
}
