package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.MagicPower; // 确保导入了你的智力Power
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class IntellectShield extends BaseCard {
    // 唯一ID
    public static final String ID = "basicmod:IntellectShield";

    // 语言包
    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    // 卡牌基本信息
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,  // 卡牌颜色
            CardType.SKILL,               // 卡牌类型
            CardRarity.UNCOMMON,              // 卡牌稀有度 (罕见)
            CardTarget.SELF,              // 目标
            1                             // 费用 1
    );

    public IntellectShield() {
        super(ID, info);

        // 基础格挡：5
        this.baseBlock = 5;

        // 基础倍率：3 (每点智力加3格挡)
        this.baseMagicNumber = 3;
        this.magicNumber = this.baseMagicNumber;

        // 固有属性：保留
        this.selfRetain = true;
    }

    /**
     * 核心逻辑：计算动态格挡数值
     * 当卡牌在手牌中时，游戏会不断调用此方法来刷新卡面数值。
     */
    @Override
    public void applyPowers() {
        // 1. 首先调用父类方法，计算基础格挡 + 敏捷(Dexterity) + 脆弱(Frail) 等原版效果
        super.applyPowers();

        // 2. 获取玩家当前的智力层数
        int intellectAmount = 0;
        if (AbstractDungeon.player.hasPower(MagicPower.POWER_ID)) {
            intellectAmount = AbstractDungeon.player.getPower(MagicPower.POWER_ID).amount;
        }

        // 3. 如果有智力，将 (智力 *倍率) 加到当前的 block 上
        if (intellectAmount > 0) {
            this.block += intellectAmount * this.magicNumber;

            // 标记数值已被修改（这会让卡面上的数字变成绿色）
            this.isBlockModified = true;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 直接使用 this.block。
        // 因为在打出卡牌的一瞬间，applyPowers 已经被调用过了，
        // 所以 this.block 里已经包含了 (基础 + 敏捷 + 智力加成) 的总和。
        addToBot(new GainBlockAction(p, p, this.block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级后，倍率从 3 变成 4
            upgradeMagicNumber(1);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new IntellectShield();
    }
}