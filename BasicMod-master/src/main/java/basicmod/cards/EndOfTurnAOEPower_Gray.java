package basicmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EndOfTurnAOEPower_Gray extends AbstractPower {
    public static final String POWER_ID = "basicmod:EndOfTurnAOEPower";

    private int remainingTurns;
    private int damage;

    public EndOfTurnAOEPower_Gray(AbstractCreature owner, int damage, int turns) {
        this.name = "End of Turn Blast";
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = damage; // 用 amount 记录每次伤害
        this.remainingTurns = turns;
        this.damage = damage;
        this.type = PowerType.BUFF;
    }

    @Override
    public void atEndOfRound() {
        // 对敌方全体造成伤害
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(owner, DamageInfo.createDamageMatrix(damage, true),
                        DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE)
        );

        // 减少剩余回合
        remainingTurns--;
        if (remainingTurns <= 0) {
            AbstractDungeon.player.powers.remove(this);
        }
    }
}

