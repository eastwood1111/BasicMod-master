package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LiSkillCard extends BaseCard {

    public static final String ID = makeID(LiSkillCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY, // 修改为 ALL_ENEMY 语义更准确
            2
    );

    private static final int DAMAGE = 2;
    private static final int HIT_COUNT = 8;
    private static final int UPG_HIT_COUNT = 2; // 升级增加 2 次，共 10 次

    public LiSkillCard() {
        super(ID, info);
        this.baseDamage = DAMAGE;
        // 将攻击次数绑定到 magicNumber
        this.baseMagicNumber = this.magicNumber = HIT_COUNT;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;

        // 检查战斗中是否打出过牌
        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.isEmpty()) {
            this.cantUseMessage = "上一张牌不是攻击牌！"; // 实际上是没打过牌
            return false;
        }

        // 获取最后一张打出的牌
        AbstractCard lastCard = AbstractDungeon.actionManager.cardsPlayedThisCombat
                .get(AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1);

        if (lastCard.type != CardType.ATTACK) {
            this.cantUseMessage = "上一张牌不是攻击牌！";
            return false;
        }

        return true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 循环次数直接取用 magicNumber
        for (int i = 0; i < this.magicNumber; i++) {
            // 使用 addToBot 配合内部逻辑，确保随机选择的是存活的敌人
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    // 重新获取随机存活目标
                    AbstractMonster randomTarget = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);

                    if (randomTarget != null) {
                        // 重新计算针对该目标的伤害（考虑易伤等）
                        calculateCardDamage(randomTarget);

                        // 执行伤害
                        addToTop(new DamageAction(
                                randomTarget,
                                new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL),
                                AttackEffect.SLASH_HEAVY
                        ));
                    }
                    this.isDone = true;
                }
            });
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级增加段数
            upgradeMagicNumber(UPG_HIT_COUNT);
            // 如果有升级描述切换，在这里处理
            // this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LiSkillCard();
    }
}