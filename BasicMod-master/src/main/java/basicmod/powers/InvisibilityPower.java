package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;  // 使用内建的无实体Power
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class InvisibilityPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:InvisibilityPower";
    public static final String NAME = "隐形身躯";
    public static final String[] DESCRIPTIONS = {
            "下回合开始时，获得 1 层无实体。"
    };

    public InvisibilityPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 1;  // 表示无实体效果的持续时间
        this.type = PowerType.BUFF;  // 因为是正面效果
        this.isTurnBased = true;

        // 图标（你可以更换为自己想要的图标）
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"),
                0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"),
                0, 0, 32, 32
        );

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        // 下回合开始时赋予 1 层无实体效果
        addToBot(new ApplyPowerAction(owner, owner, new IntangiblePower(owner, 1), 1));

        // 使用后立即移除该Power
        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new InvisibilityPower(owner);
    }
}
