package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ReactiveGuard extends BaseCard {
    public static final String ID = makeID(ReactiveGuard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );

    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 8;

    public ReactiveGuard() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 基础格挡
        addToBot(new GainBlockAction(p, p, block));

        // 检查本回合是否使用过消耗牌
        boolean consumedCardPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                .anyMatch(c -> c.cost == -1 || c.costForTurn == -1);

        if (consumedCardPlayed) {
            addToBot(new GainBlockAction(p, p, 5)); // 额外格挡固定5点
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK - BLOCK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ReactiveGuard();
    }
}
