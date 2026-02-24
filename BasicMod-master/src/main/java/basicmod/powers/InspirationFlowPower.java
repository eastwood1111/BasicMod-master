package basicmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class InspirationFlowPower extends AbstractPower {
    public static final String POWER_ID = "soul-five:InspirationFlowPower";

    private int drawAmount;

    public InspirationFlowPower(AbstractCreature owner, int drawAmount) {
        this.name = "Inspiration Flow";
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = drawAmount;
        this.drawAmount = drawAmount;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // 使用统一纹理 example
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (owner != null) {
            AbstractDungeon.actionManager.addToBottom(
                    new DrawCardAction(owner, drawAmount)
            );
        }
    }

    @Override
    public void onRemove() {
        // 安全移除，不做任何操作
    }

    @Override
    public void updateDescription() {
        this.description = "每回合开始时，额外抽 " + drawAmount + " 张牌。";
    }
}
