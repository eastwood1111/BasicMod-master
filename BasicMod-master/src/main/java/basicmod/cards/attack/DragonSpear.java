package basicmod.cards.attack;

import basicmod.cards.BaseCard;
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

public class DragonSpear extends BaseCard {

    public static final String ID = "basicmod:DragonSpear";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ALL_ENEMY,
            1
    );

    private static final int DAMAGE = 7;
    private static final int UPGRADE_DAMAGE = 10;

    public DragonSpear() {
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

        // 2. 【快照】记录动作开始前，场上还活着的敌人数量
        int monstersAliveBefore = 0;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDeadOrEscaped()) {
                monstersAliveBefore++;
            }
        }
        // 由于内部类需要 final 变量，我们把计数存入 final 变量
        final int initialCount = monstersAliveBefore;

        // 3. 【第一段伤害】同时对所有敌人造成伤害
        // 使用 DamageAllEnemiesAction 实现“同时”打击
        addToBot(new DamageAllEnemiesAction(
                p,
                this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL
        ));

        // 4. 【判定逻辑】添加一个后续动作来检查结果
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                // 再次统计现在活着的敌人数量
                int monstersAliveAfter = 0;
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    // 这里使用 strict 判定：只要没死透就不算死
                    if (!mo.isDeadOrEscaped()) {
                        monstersAliveAfter++;
                    }
                }

                // 【核心逻辑】如果现在的数量 < 之前的数量，说明有人死了（斩杀成功）
                if (monstersAliveAfter < initialCount) {

                    // 再次造成全体伤害（或者你可以改成只对剩下的敌人造成伤害）
                    // 这里演示的是：如果斩杀，就再次释放一次全体攻击（类似诺克萨斯断头台的效果）
                    addToTop(new DamageAllEnemiesAction(
                            p,
                            DragonSpear.this.multiDamage,
                            DragonSpear.this.damageTypeForTurn,
                            AbstractGameAction.AttackEffect.SLASH_HEAVY // 换个特效区分一下
                    ));
                }

                // 动作完成，必须调用
                this.isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE - DAMAGE);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DragonSpear();
    }
}