package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class DivineRacePower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:DivineRacePower";
    public static final String NAME = "神族";
    public static final String[] DESCRIPTIONS = {"下回合将被遣返"};

    public DivineRacePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        // 占位图，确保路径存在
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.amount -= 1;

        if (this.amount <= 0) {
            // 层数归0时，对自己造成大量伤害，保证死亡
            addToBot(new LoseHPAction(owner, owner, 99999));
            // 移除自身
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + " 当前层数: " + this.amount;
    }

    @Override
    public AbstractPower makeCopy() {
        return new DivineRacePower(owner, amount);
    }
}
