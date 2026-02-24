package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LiSkillCard extends BaseCard {

    public static final String ID = makeID(LiSkillCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY,
            2
    );

    private static final int DAMAGE = 2;
    private static final int HIT_COUNT = 8;
    private static final int UPG_HIT_COUNT = 2;

    public LiSkillCard() {
        super(ID, info);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = HIT_COUNT;
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;

        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.isEmpty()) {
            this.cantUseMessage = "上一张牌不是攻击牌！";
            return false;
        }

        AbstractCard lastCard = AbstractDungeon.actionManager.cardsPlayedThisCombat
                .get(AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1);

        if (lastCard.type != CardType.ATTACK) {
            this.cantUseMessage = "上一张牌不是攻击牌！";
            return false;
        }

        return true;
    }

    // 建议加上发光提示，保持体验一致
    @Override
    public void triggerOnGlowCheck() {
        if (!AbstractDungeon.actionManager.cardsPlayedThisCombat.isEmpty() &&
                AbstractDungeon.actionManager.cardsPlayedThisCombat.get(AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1).type == CardType.ATTACK) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用原版自带的随机攻击 Action，它非常稳定
        for (int i = 0; i < this.magicNumber; i++) {
            // 参数说明：(卡牌本身, 攻击特效)
            // 它会自动处理 calculateCardDamage 和随机寻找存活目标
            addToBot(new AttackDamageRandomEnemyAction(this, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_HIT_COUNT);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LiSkillCard();
    }
}