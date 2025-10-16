package basicmod.cards;

import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

public class TacticalReorder extends BaseCard {
    public static final String ID = makeID(TacticalReorder.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );

    private static final int DRAW = 3;
    private static final int UPG_DRAW = 4;

    public TacticalReorder() {
        super(ID, info);
        setMagic(DRAW, UPG_DRAW);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int handCount = p.hand.size();
        if (handCount <= 0) {
            addToBot(new DrawCardAction(p, magicNumber));
            return;
        }

        final int discardAmount = Math.max(handCount / 2, 1);

        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                    // 打开手牌选择界面
                    AbstractDungeon.handCardSelectScreen.open(
                            "选择要弃掉的 " + discardAmount + " 张牌。",
                            discardAmount,
                            false,
                            false
                    );
                    this.isDone = true; // 动作完成，后续由手牌选择屏幕处理
                } else {
                    // 获取选择的牌
                    Iterator<AbstractCard> it = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();
                    while (it.hasNext()) {
                        AbstractCard c = it.next();
                        addToTop(new DiscardSpecificCardAction(c, p.hand));
                    }
                    AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
                    AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;

                    addToBot(new WaitAction(0.1F));
                    addToBot(new DrawCardAction(p, magicNumber));

                    this.isDone = true;
                }
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_DRAW - DRAW);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TacticalReorder();
    }
}
