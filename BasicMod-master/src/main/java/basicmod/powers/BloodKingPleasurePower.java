package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class BloodKingPleasurePower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = "basicmod:BloodKingPleasurePower";
    public static final String NAME = "鲜血君王的欢愉";

    public BloodKingPleasurePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/BloodKingPleasureCard.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/BloodKingPleasureCard.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    // 当敌人失去出血状态时调用
    public void onEnemyBleedRemoved(AbstractCreature enemy) {
        flash();
        // 仅增加力量
        addToBot(new ApplyPowerAction(owner, owner,
                new com.megacrit.cardcrawl.powers.StrengthPower(owner, this.amount),
                this.amount));
    }

    @Override
    public void updateDescription() {
        this.description = "每当敌人失去出血状态时，获得 #b" + this.amount + " 点力量。";
    }

    @Override
    public AbstractPower makeCopy() {
        return new BloodKingPleasurePower(owner, amount);
    }
}
