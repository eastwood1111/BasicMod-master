package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EnduranceThisAndNextTurnPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:EnduranceThisAndNextTurnPower";
    public static final String NAME = "忍耐";
    public static final String[] DESCRIPTIONS = {
            "本回合及下回合受到的伤害减半。"
    };

    private int turnsRemaining; // 剩余回合数

    public EnduranceThisAndNextTurnPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.turnsRemaining = 2; // 当前回合 + 下回合

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            return damageAmount / 2;
        }
        return damageAmount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        turnsRemaining--;
        if (turnsRemaining <= 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(owner, owner, this.ID)
            );
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new EnduranceThisAndNextTurnPower(owner);
    }
}
