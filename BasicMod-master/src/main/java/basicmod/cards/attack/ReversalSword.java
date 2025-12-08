package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.BleedPower;  // 引入出血Power
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class ReversalSword extends BaseCard {  // "反手剑" 卡牌

    public static final String ID = "basicmod:ReversalSword";  // 卡牌ID
    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            0  // 费用 0
    );

    private static final int DAMAGE = 5;  // 初始伤害 5
    private static final int BLEED_LAYERS = 2;  // 初始出血层数 2
    private static final int UPGRADE_BLEED_LAYERS = 3;  // 升级后出血层数 3

    public ReversalSword() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.baseDamage = DAMAGE;
        this.damage = this.baseDamage;
        this.exhaust = false;  // 卡牌不消耗
        initializeDescription();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        // 调用父类的 canUse 方法，确保基本条件满足
        if (!super.canUse(p, m)) return false;

        // 获取上一张打出的牌
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty()) {
            this.cantUseMessage = "上一张牌不是攻击牌，无法打出！";
            return false;
        }

        // 获取上一张打出的牌
        AbstractCard lastCard = AbstractDungeon.actionManager.cardsPlayedThisTurn
                .get(AbstractDungeon.actionManager.cardsPlayedThisTurn.size() - 1);

        // 判断上一张打出的牌是否是攻击牌
        if (lastCard.type != CardType.ATTACK) {
            this.cantUseMessage = "上一张牌不是攻击牌，无法打出！";
            return false;
        }

        // 如果上一张卡牌是攻击卡，则返回 true，表示可以使用
        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用 DamageInfo 来造成伤害
        DamageInfo damageInfo = new DamageInfo(p, 5, DamageInfo.DamageType.NORMAL);  // 假设伤害是 5

        // 造成伤害
        addToBot(new DamageAction(m, damageInfo, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        // 给予 BleedPower（出血）层数，假设给予 2 层出血
        addToBot(new ApplyPowerAction(m, p, new BleedPower(m, 2), 2));
    }



    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级后增加出血层数
            upgradeMagicNumber(UPGRADE_BLEED_LAYERS - BLEED_LAYERS);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ReversalSword();
    }
}
