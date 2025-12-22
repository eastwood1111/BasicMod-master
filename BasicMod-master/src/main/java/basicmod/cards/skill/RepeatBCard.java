package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import basicmod.powers.RepeatNextCardPower;

public class RepeatBCard extends BaseCard {
    // 唯一ID
    public static final String ID = "basicmod:RepeatBCard";

    // 语言包
    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    // 卡牌基本信息
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,  // 卡牌颜色
            CardType.SKILL,                // 卡牌类型
            CardRarity.RARE,              // 卡牌稀有度
            CardTarget.SELF,              // 目标
            1                             // 费用 1
    );

    public RepeatBCard() {
        super(ID, info);
        // 不需要在构造函数里手动设置 name/desc，BaseCard通常会处理，
        // 但如果你的BaseCard没处理，保留这两行也没错。
        // 建议：确保 strings.json 里有 ID 对应的文本
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 升级后，给予 2 层 Power；未升级给予 1 层。
        // 逻辑意图：每一层 Power 允许下一张打出的卡重复一次。
        int amount = this.upgraded ? 2 : 1;

        addToBot(new ApplyPowerAction(p, p, new RepeatNextCardPower(p, amount), amount));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级后描述变化
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RepeatBCard();
    }
}