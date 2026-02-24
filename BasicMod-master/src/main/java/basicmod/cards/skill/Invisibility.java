package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.InvisibilityPower;
import basicmod.powers.CurrentStancePower;
import basicmod.util.CardStats;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Invisibility extends BaseCard {
    public static final String ID = makeID(Invisibility.class.getSimpleName());
    private static final String IMG = "basicmod/images/cards/skill/default.png";

    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1  // 1 费
    );

    // 定义数值常量
    private static final int BLOCK = 15;
    private static final int INTANGIBLE_AMT = 1;

    public Invisibility() {
        super(ID, info);

        // 使用 BaseCard 提供的便捷方法设置数值
        setBlock(BLOCK);
        setMagic(INTANGIBLE_AMT);

        this.exhaust = true; // 初始时消耗
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // --- 逻辑修改：无论如何都先获得格挡 ---
        addToBot(new GainBlockAction(p, p, this.block));

        // 获取当前的姿态
        CurrentStancePower stance = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);

        // 判断是否处于魔法架势
        if (stance != null && stance.getCurrentStance() == CurrentStancePower.Stance.MAGIC) {
            // 如果处于魔法架势，额外赋予无实体效果
            // 注意：这里假设你的 InvisibilityPower 构造函数支持 (owner, amount)
            addToBot(new ApplyPowerAction(p, p, new InvisibilityPower(p), 1));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级逻辑：移除消耗
            this.exhaust = false;

            // 如果你想升级时也增加格挡，可以取消下面这行的注释
            // upgradeBlock(5);

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Invisibility();
    }
}