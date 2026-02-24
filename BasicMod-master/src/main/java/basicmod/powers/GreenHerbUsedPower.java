package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class GreenHerbUsedPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "soul-five:GreenHerbUsedPower";
    public static final String NAME = "绿花草使用限制";
    public static final String[] DESCRIPTIONS = {
            "本回合已使用过绿花草，无法再次使用。"
    };

    public GreenHerbUsedPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF; // 用 DEBUFF 表示限制
        this.isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    // 回合开始移除 Power
    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(
                new RemoveSpecificPowerAction(owner, owner, this.ID)
        );
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new GreenHerbUsedPower(owner);
    }
}
