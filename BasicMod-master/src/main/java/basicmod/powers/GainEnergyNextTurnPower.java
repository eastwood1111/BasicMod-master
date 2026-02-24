package basicmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class GainEnergyNextTurnPower extends AbstractPower {
    public static final String POWER_ID = "soul-five:GainEnergyNextTurnPower";

    public GainEnergyNextTurnPower(AbstractCreature owner, int amount) {
        this.name = "下回合获得能量";
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;  // 下回合获得的能量数量
        this.type = PowerType.BUFF;
        this.isTurnBased = false;  // 使其在下回合触发

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = "下回合获得 " + this.amount + " 能量";  // 显示下一回合获得的能量数量
    }

    @Override
    public void atStartOfTurn() {
        // 在下回合开始时为玩家提供能量
        addToBot(new com.megacrit.cardcrawl.actions.common.GainEnergyAction(this.amount));

        if (this.owner instanceof AbstractPlayer) {
            AbstractPlayer p = (AbstractPlayer) this.owner;
            addToBot(new RemoveSpecificPowerAction(p, p, this.ID));
        }
    }
}
