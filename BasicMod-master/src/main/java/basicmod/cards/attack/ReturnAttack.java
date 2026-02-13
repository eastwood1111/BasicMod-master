package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import basemod.ReflectionHacks;

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
        this.baseDamage = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // use 之前 calculateCardDamage 会被调用，所以直接用 this.damage
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_HEAVY
        ));
    }

    // 【关键修复】重写此方法，让卡牌在指向敌人时实时显示伤害
    @Override
    public void calculateCardDamage(AbstractMonster m) {
        updateBaseDamageFromIntent(m);
        super.calculateCardDamage(m);
    }

    // 【关键修复】当卡牌在手中，没有指向敌人时，伤害归零或保持基础
    @Override
    public void applyPowers() {
        this.baseDamage = 0;
        super.applyPowers();
    }

    private void updateBaseDamageFromIntent(AbstractMonster m) {
        if (m != null && (m.intent == AbstractMonster.Intent.ATTACK ||
                m.intent == AbstractMonster.Intent.ATTACK_BUFF ||
                m.intent == AbstractMonster.Intent.ATTACK_DEBUFF ||
                m.intent == AbstractMonster.Intent.ATTACK_DEFEND)) {

            // 直接获取怪物意图面板上的单段伤害（已计入怪物力量和虚弱）
            int multiAmt = 1;
            boolean isMultiDmg = ReflectionHacks.getPrivate(m, AbstractMonster.class, "isMultiDmg");
            if (isMultiDmg) {
                multiAmt = ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
            }

            int singleDmg = ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentDmg");
            this.baseDamage = singleDmg * multiAmt;
        } else {
            this.baseDamage = 0;
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(1);
            // 建议在 description 里写 "!D!" 来动态显示伤害
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}