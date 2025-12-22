package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DoubleDebuff extends BaseCard {
    public static final String ID = "basicmod:DoubleDebuff";
    private static final String IMG = "basicmod/images/cards/skill/default.png";

    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1
    );

    public DoubleDebuff() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        // this.cost = 1; // 父类 BaseCard 已经根据 info 设置了 cost，这里可以省略，除非你想覆盖
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 遍历敌方所有 Power
        for (AbstractPower power : m.powers) {
            // 1. 检查是否为负面效果 (Debuff)
            // 2. 检查 ID 是否为 "Shackled" (被枷锁)，通常不建议翻倍这个，因为它会在回合结束归还力量
            // 3. 排除不可叠加的 Power (amount通常为-1)，翻倍没有意义
            if (power.type == AbstractPower.PowerType.DEBUFF && power.amount != 0) {

                // === 核心修复逻辑 ===

                // 直接将数值乘以 2
                power.amount *= 2;

                // 让 Power 闪烁一下，提供视觉反馈
                power.flash();

                // 更新 Power 的描述文本 (例如：从 "每回合扣除5点" 变成 "每回合扣除10点")
                power.updateDescription();
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0); // 推荐使用 upgradeBaseCost 方法，它会自动处理描述颜色等细节
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DoubleDebuff();
    }
}