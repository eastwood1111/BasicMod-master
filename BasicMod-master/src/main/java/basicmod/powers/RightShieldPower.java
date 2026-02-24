package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

/**
 * 右盾（RightShieldPower）
 * 每回合开始时，层数 +1，达到阈值时触发无实体并重置计数。
 */
public class RightShieldPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "soul-five:RightShieldPower";
    public static final String NAME = "右盾";
    public static final String[] DESCRIPTIONS = {
            "每回合获得 1 层计数，当计数达到 #b",
            " 时，获得 #b1 层无实体，并重置计数。"
    };

    // interval 表示阈值，例如 5 回合触发一次
    private final int interval;

    public RightShieldPower(AbstractCreature owner, int interval) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 0; // 当前计数层数
        this.interval = interval;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/RightShieldCard.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/RightShieldCard.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        // 每回合计数 +1
        this.amount++;

        // 达到阈值，触发无实体并重置
        if (this.amount >= this.interval) {
            this.amount = 0;
            addToBot(new ApplyPowerAction(owner, owner, new IntangiblePlayerPower(owner, 1), 1));
        }

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.interval + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new RightShieldPower(owner, interval);
    }
}
