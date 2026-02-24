package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.cards.skill.AdorationRing;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction; // 核心：全体伤害动作
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class GudaSpear extends BaseCard {

    public static final String ID = makeID(GudaSpear.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ALL_ENEMY,
            1
    );

    private static final int DAMAGE = 2; // 初始伤害
    private static final int UPGRADE_DAMAGE = 3; // 升级后伤害
    private static final int HITS = 3; // 攻击次数

    public GudaSpear() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.baseDamage = DAMAGE;

        // 【重要】开启多重伤害标记，这样游戏才会自动计算 multiDamage 数组
        this.isMultiDamage = true;

        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 确保伤害数值是最新的（计算力量、易伤等加成）
        this.calculateCardDamage(null);

        // 2. 对所有敌人造成伤害，执行 3 次伤害
        for (int i = 0; i < HITS; i++) {
            addToBot(new DamageAllEnemiesAction(
                    p,
                    this.multiDamage,
                    this.damageTypeForTurn,
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL
            ));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE - DAMAGE); // 增加伤害
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GudaSpear();
    }
}
