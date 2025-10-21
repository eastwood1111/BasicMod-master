package basicmod.cards.skill;

import basicmod.util.CardStats;
import basicmod.charater.MyCharacter;
import basicmod.cards.BaseCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static basicmod.BasicMod.makeID;

public class PageFragment extends BaseCard {
    public static final String ID = makeID(PageFragment.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.SPECIAL,
            CardTarget.SELF,
            0
    );

    public PageFragment() {
        super(ID, info);
        this.exhaust = true; // 用后消耗
        this.selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CardGroup discardPile = p.discardPile;
        int amount = this.upgraded ? 2 : 1;

        if (discardPile.isEmpty()) {
            return;
        }

        // 打开弃牌堆选择界面
        AbstractDungeon.gridSelectScreen.open(
                discardPile,
                Math.min(amount, discardPile.size()),
                "选择要返回手牌的牌",
                false
        );

        // 等待玩家选择后执行
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                    for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                        p.discardPile.removeCard(c);
                        c.unhover();
                        c.retain = true; // 本回合保留
                        p.hand.addToHand(c);
                        c.lighten(true);
                        c.applyPowers();
                    }
                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    p.hand.refreshHandLayout();
                }
                isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PageFragment();
    }
}
