package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class StanceDefendPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:StanceDefendPower";
    public static final String NAME = "架势防御";

    public StanceDefendPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32);

        updateDescription();
    }

    // --- 核心修改：统一触发接口 ---
    public void onStanceTrigger() {
        this.flash();
        // 每次进入架势获得等同于层数的格挡
        addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0f;
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = "每次进入架势时，获得 #b" + this.amount + " 点格挡。";
    }

    @Override
    public AbstractPower makeCopy() {
        return new StanceDefendPower(owner, amount);
    }
}