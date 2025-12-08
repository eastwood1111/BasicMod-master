package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.BleedPower;  // 引入出血 Power
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

public class Blood extends BaseCard {  // 卡牌名称为血
    public static final String ID = "basicmod:Blood";  // 卡牌ID
    private static final String IMG = "basicmod/images/cards/skill/default.png";  // 图片路径

    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1  // 1费
    );

    private static final int BLEED_AMOUNT = 3;  // 初始给予敌人 3 层出血
    private static final int UPGRADE_BLEED_AMOUNT = 5;  // 升级后给予敌人 5 层出血

    public Blood() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();

        // 初始化其他属性
        this.exhaust = false;  // 不消耗
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用卡牌时，给敌方施加出血状态
        int bleedAmount = BLEED_AMOUNT;  // 初始为3层

        // 升级后，施加5层出血
        if (this.upgraded) {
            bleedAmount = UPGRADE_BLEED_AMOUNT;
        }

        // 给敌人施加出血状态
        addToBot(new ApplyPowerAction(m, p, new BleedPower(m, bleedAmount), bleedAmount));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级后，增加出血层数
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Blood();
    }
}
