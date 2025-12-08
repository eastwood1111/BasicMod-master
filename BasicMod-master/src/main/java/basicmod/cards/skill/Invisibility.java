package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.InvisibilityPower;  // 无实体Power
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
    public static final String ID = "basicmod:Invisibility";
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

    private static final int MAGIC_MODE_EXTRA = 1; // 魔法架势额外的效果

    public Invisibility() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();

        // 初始化其他属性，如是否消耗、伤害、等
        this.exhaust = true; // 初始时消耗
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获取当前的姿态
        CurrentStancePower stance = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);

        // 判断是否处于魔法架势
        if (stance != null && stance.getCurrentStance() == CurrentStancePower.Stance.MAGIC) {
            // 如果处于魔法架势，赋予无实体效果
            addToBot(new ApplyPowerAction(p, p, new InvisibilityPower(p), 1));
        } else {
            // 如果不在魔法架势，可以选择跳过或做其他逻辑
            addToBot(new GainBlockAction(p, p, 15));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级后移除消耗
            this.exhaust = false;

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Invisibility();
    }
}
