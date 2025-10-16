package basicmod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DoubleNextZeroCostPower_Gray extends AbstractPower {
    public static final String POWER_ID = "basicmod:DoubleNextZeroCostPower";

    private int remainingZeroCards;

    public DoubleNextZeroCostPower_Gray(AbstractCreature owner, int amount) {
        this.name = "Double Next 0-Cost";
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.remainingZeroCards = amount;
        this.type = PowerType.BUFF;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.costForTurn == 0) {
            flash();
            AbstractCard copy = card.makeStatEquivalentCopy();
            AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(copy, 1, false));
            remainingZeroCards--;
            if (remainingZeroCards <= 0) {
                AbstractDungeon.player.powers.remove(this);
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 回合结束移除剩余Power
        AbstractDungeon.player.powers.remove(this);
    }
}
