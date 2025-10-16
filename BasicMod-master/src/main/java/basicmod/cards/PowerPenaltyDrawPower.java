package basicmod.cards;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PowerPenaltyDrawPower extends AbstractPower {
    public static final String POWER_ID = "basicmod:PowerPenaltyDrawPower";

    private int strengthAmount;
    private int dexAmount;

    public PowerPenaltyDrawPower(AbstractCreature owner, int strength, int dex) {
        this.name = "Power Surge with Draw Penalty";
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.strengthAmount = strength;
        this.dexAmount = dex;
    }

    @Override
    public void atStartOfTurn() {
        // 每回合少摸1张牌
        AbstractDungeon.player.gameHandSize -= 1;

        // 增加力量和敏捷
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strengthAmount), strengthAmount));
        addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, dexAmount), dexAmount));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 回合结束后恢复手牌上限
        AbstractDungeon.player.gameHandSize += 1;
    }
}

