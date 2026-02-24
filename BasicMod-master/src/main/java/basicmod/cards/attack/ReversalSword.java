package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.cards.skill.AdorationRing;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.BleedPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ReversalSword extends BaseCard {

    public static final String ID = makeID(ReversalSword.class.getSimpleName());
    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            0
    );

    private static final int DAMAGE = 5;
    private static final int UPG_DAMAGE = 2; // 升级增加的伤害
    private static final int BLEED_LAYERS = 2;

    public ReversalSword() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;

        this.baseDamage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = BLEED_LAYERS;

        this.exhaust = false;
        initializeDescription();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;

        // 检查整场战斗的记录
        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.isEmpty()) {
            this.cantUseMessage = "上一张牌不是攻击牌，无法打出！";
            return false;
        }

        AbstractCard lastCard = AbstractDungeon.actionManager.cardsPlayedThisCombat
                .get(AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1);

        if (lastCard.type != CardType.ATTACK) {
            this.cantUseMessage = "上一张牌不是攻击牌，无法打出！";
            return false;
        }

        return true;
    }

    @Override
    public void triggerOnGlowCheck() {
        // 如果满足使用条件（上一张是攻击牌），则显示金色边框
        if (!AbstractDungeon.actionManager.cardsPlayedThisCombat.isEmpty()) {
            AbstractCard lastCard = AbstractDungeon.actionManager.cardsPlayedThisCombat
                    .get(AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1);

            if (lastCard.type == CardType.ATTACK) {
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                return;
            }
        }
        // 否则恢复默认蓝色
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用 this.damage 和 this.magicNumber 确保数值正确应用
        addToBot(new DamageAction(m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        addToBot(new ApplyPowerAction(m, p,
                new BleedPower(m, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级：仅增加伤害
            upgradeDamage(UPG_DAMAGE);

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ReversalSword();
    }
}