package basicmod.cards;

import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class DefenseConversion extends BaseCard {
    public static final String ID = makeID(DefenseConversion.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, // 蓝色角色
            CardType.SKILL,              // 技能牌
            CardRarity.UNCOMMON,         // 蓝色稀有度
            CardTarget.SELF,             // 作用于自己
            1                            // 能量消耗
    );

    private static final int BLOCK_PER = 3;
    private static final int UPG_BLOCK_PER = 4;

    public DefenseConversion() {
        super(ID, info);

        baseBlock = BLOCK_PER;
        upgradeBlock(0); // 初始化
        this.exhaust = true; // 默认使用后消耗
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 找出所有技能牌（不包括本身）
        ArrayList<AbstractCard> skillsToDiscard = new ArrayList<>();
        for (AbstractCard c : p.hand.group) {
            if (c != this && c.type == CardType.SKILL) {
                skillsToDiscard.add(c);
            }
        }

        int discardCount = skillsToDiscard.size();

        // 丢弃技能牌
        for (AbstractCard c : skillsToDiscard) {
            p.hand.moveToDiscardPile(c);
            c.triggerOnManualDiscard();
        }

        // 根据弃牌数量获得格挡
        if (discardCount > 0) {
            addToBot(new GainBlockAction(p, p, discardCount * this.block));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK_PER - BLOCK_PER); // 升级后改为每张4点格挡
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DefenseConversion();
    }
}

