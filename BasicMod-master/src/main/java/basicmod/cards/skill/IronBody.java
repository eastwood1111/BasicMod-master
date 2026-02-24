package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.VoidCard; // 注意：原版Void类的名称通常是VoidCard或者直接位于status包下的Void
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class IronBody extends BaseCard {
    public static final String ID = makeID(IronBody.class.getSimpleName());
    private static final String IMG = "basicmod/images/cards/skill/default.png";

    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 12;
    private static final int UPGRADE_BLOCK = 15;

    public IronBody() {
        super(ID, info);

        // 设置基本属性
        this.baseBlock = BLOCK;

        // 设置预览卡牌：让玩家把鼠标悬停在这张卡上时，能看到“虚空”长什么样
        this.cardsToPreview = new com.megacrit.cardcrawl.cards.status.VoidCard();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 获得格挡
        // 注意：BaseCard通常已经处理了upgradeBlock的逻辑，所以不需要手动判断if(upgraded)来赋值
        // 直接使用 this.block 即可，游戏会自动计算基础值+升级值+敏捷加成
        addToBot(new GainBlockAction(p, p, this.block));

        // 2. 将虚空卡加入弃牌堆
        // MakeTempCardInDiscardAction 的参数：(卡牌对象, 数量)
        // 这个动作会自动处理飞入弃牌堆的动画
        addToBot(new MakeTempCardInDiscardAction(new com.megacrit.cardcrawl.cards.status.VoidCard(), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 使用 upgradeBlock 方法增加差值，这样能正确配合 BaseCard 的逻辑
            upgradeBlock(UPGRADE_BLOCK - BLOCK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new IronBody();
    }
}