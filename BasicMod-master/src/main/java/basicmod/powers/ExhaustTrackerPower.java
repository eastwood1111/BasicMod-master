package basicmod.powers;

import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class ExhaustTrackerPower extends AbstractPower {
    public boolean exhaustedThisTurn = false;

    public ExhaustTrackerPower(AbstractPlayer owner) {
        this.name = "Exhaust Tracker";
        this.ID = "ExhaustTrackerPower";
        this.owner = owner;
        this.amount = 0;
        this.isTurnBased = false;
        this.type = PowerType.BUFF;
        this.loadRegion("none"); // 完全透明
    }

    // 被消耗时调用
    public void onExhaustedOnce() {
        exhaustedThisTurn = true;
    }

    @Override
    public void atStartOfTurn() {
        exhaustedThisTurn = false;
    }
}
