package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class DiscardDrawFive extends BaseCard {
    public static final String ID = makeID(DiscardDrawFive.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, // 卡牌颜色（你角色的颜色）
            CardType.SKILL,              // 技能牌
            CardRarity.UNCOMMON,         // 蓝色稀有度 -> UNCOMMON
            CardTarget.SELF,
            0                            // 0 能量
    );

    private static final int DISCARD = 5;
    private static final int DRAW = 5;

    public DiscardDrawFive() {
        super(ID, info);

        // 这张牌会在使用后消耗（exhaust），默认行为
        this.exhaust = true;

        // 如果你需要在卡牌描述里显示常量，可以使用 baseMagicNumber 等（此处不必）
        // this.baseMagicNumber = this.magicNumber = ...;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 先弃牌（false 表示不是随机弃牌），再摸牌
        addToBot(new DiscardAction(p, p, DISCARD, false));
        addToBot(new DrawCardAction(p, DRAW));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();

            // 升级移除消耗效果（升级后不再 exhaust）
            this.exhaust = false;

            // 更新描述为升级描述（从本地化里读取）
            if (cardStrings != null && cardStrings.UPGRADE_DESCRIPTION != null && !cardStrings.UPGRADE_DESCRIPTION.isEmpty()) {
                this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            }
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DiscardDrawFive();
    }
}
