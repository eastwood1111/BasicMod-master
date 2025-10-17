package basicmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class EndOfTurnAOEPower extends AbstractPower {
    public static final String POWER_ID = "basicmod:EndOfTurnAOEPower";

    private int remainingTurns;
    private int damage;

    public EndOfTurnAOEPower(AbstractCreature owner, int damage, int turns) {
        this.name = "End of Turn Blast";
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = damage;
        this.damage = damage;
        this.remainingTurns = turns;
        this.type = PowerType.BUFF;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        // 如果敌人存在才造成伤害
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAllEnemiesAction(owner, DamageInfo.createDamageMatrix(damage, true),
                            DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE)
            );
        }

        remainingTurns--;
        if (remainingTurns <= 0) {
            // 使用 Action 安全移除自身
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(owner, owner, this)
            );
        }
    }

    @Override
    public void updateDescription() {
        this.description = "每回合结束时，对敌方全体造成 " + damage + " 点伤害，持续 " + remainingTurns + " 回合。";
    }
}
