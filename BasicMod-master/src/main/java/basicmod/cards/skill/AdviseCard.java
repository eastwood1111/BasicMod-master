package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class AdviseCard extends BaseCard {

    public static final String ID = "basicmod:AdviseCard";
    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,  // 卡牌颜色
            CardType.SKILL,  // 卡牌类型
            CardRarity.BASIC,  // 卡牌稀有度
            CardTarget.SELF,  // 目标
            0  // 费用 0
    );

    public AdviseCard() {
        super(ID, info);  // 使用 info 初始化 BaseCard

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;

        // 本卡为消耗卡，升级后取消消耗
        this.exhaust = true;

        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 根据是否升级，决定抽取的卡牌数量
        int drawAmount = this.upgraded ? 3 : 2;
        addToBot(new DrawCardAction(drawAmount));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级后改变描述为抽3张
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new AdviseCard();
    }
}
