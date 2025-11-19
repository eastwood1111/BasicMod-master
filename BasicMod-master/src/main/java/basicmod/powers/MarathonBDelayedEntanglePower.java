package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class MarathonBDelayedEntanglePower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:MarathonBDelayedEntanglePower";
    public static final String NAME = "延迟缠身";
    public static final String[] DESCRIPTIONS = {
            "下回合开始获得一层缠身。"
    };

    public MarathonBDelayedEntanglePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 1;
        this.type = PowerType.DEBUFF; // 因为是负面效果
        this.isTurnBased = true;

        // 图标（可替换成你的自定义图片）
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
        // 下回合开始赋予 1 层缠身
        addToBot(new ApplyPowerAction(owner, owner, new EntanglePower(owner), 1));

        // 使用后立即移除
        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new MarathonBDelayedEntanglePower(owner);
    }
}
