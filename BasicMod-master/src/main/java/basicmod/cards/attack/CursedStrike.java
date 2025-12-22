package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo; // 必须导入这个
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CursedStrike extends BaseCard {
    public static final String ID = "basicmod:CursedStrike";
    private static final String IMG = "basicmod/images/cards/attack/default.png";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    private static final int DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 8;

    public CursedStrike() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;

        this.baseDamage = DAMAGE;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // --- 修复部分开始 ---
        // 使用原生的 DamageInfo 构造函数：
        // 参数1: 伤害来源 (p)
        // 参数2: 伤害数值 (this.damage) - 注意使用 damage 而不是 baseDamage，以应用力量等加成
        // 参数3: 伤害类型 (this.damageTypeForTurn)
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));
        // --- 修复部分结束 ---

        if (hasNegativeBuff(m)) {
            addToBot(new DrawCardAction(p, 1));
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CursedStrike();
    }

    private boolean hasNegativeBuff(AbstractMonster m) {
        if (m == null || m.powers == null) return false;
        for (AbstractPower power : m.powers) {
            if (power.type == AbstractPower.PowerType.DEBUFF) {
                return true;
            }
        }
        return false;
    }
}