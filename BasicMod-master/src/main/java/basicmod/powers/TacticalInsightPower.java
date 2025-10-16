package basicmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TacticalInsightPower extends AbstractPower {
    public static final String POWER_ID = "basicmod:TacticalInsightPower";

    private boolean upgraded;

    public TacticalInsightPower(AbstractCreature owner, boolean upgraded) {
        this.name = "Tactical Insight";
        this.ID = POWER_ID;
        this.owner = owner;
        this.upgraded = upgraded;
        this.type = PowerType.BUFF;
        this.amount = 1; // 默认每回合抽牌数（升级后在卡牌逻辑修改）
    }

    @Override
    public void atStartOfTurn() {
        int drawAmount = upgraded ? 2 : 1;
        addToBot(new DrawCardAction(drawAmount));

        boolean skillPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                .anyMatch(card -> card.type == AbstractCard.CardType.SKILL);

        if (skillPlayed) {
            addToBot(new GainBlockAction(owner, owner, 1));
        }
    }
}
