package basicmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class StrengthBoostPower extends AbstractPower {
    public static final String POWER_ID = "basicmod:StrengthBoostPower";

    private int strengthPerTurn;

    public StrengthBoostPower(AbstractCreature owner, int strengthPerTurn) {
        this.name = "Strength Boost";
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = strengthPerTurn;
        this.strengthPerTurn = strengthPerTurn;
        this.type = PowerType.BUFF;
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthPerTurn), strengthPerTurn));
    }
}

