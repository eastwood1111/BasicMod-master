package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction; // 1. 需要导入这个动作
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SeekAdvice extends BaseCard {
    public static final String ID = "basicmod:SeekAdvice";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    public SeekAdvice() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 先抽 2 张牌
        addToBot(new DrawCardAction(2));

        // 2. 创建“谏言”卡
        AbstractCard adviseCard = new AdviseCard();

        // (可选优化) 如果这张卡升级了，生成的谏言卡通常也应该升级
        if (this.upgraded) {
            adviseCard.upgrade();
        }

        // 3. 使用 MakeTempCardInDrawPileAction 来添加卡牌并播放动画
        // 参数说明: (卡牌对象, 数量, 是否随机位置, 是否自动定位视觉效果)
        addToBot(new MakeTempCardInDrawPileAction(adviseCard, 1, true, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SeekAdvice();
    }
}