package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class MagicPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = "soul-five:MagicPower";

    public MagicPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;

        this.ID = POWER_ID;
        this.name = "智力";
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/MagicPower.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/MagicPower.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = "增加魔法祷告牌伤害，每点智力增加 1 点伤害。";
    }

    @Override
    public AbstractPower makeCopy() {
        return new MagicPower(owner, amount);
    }
}
