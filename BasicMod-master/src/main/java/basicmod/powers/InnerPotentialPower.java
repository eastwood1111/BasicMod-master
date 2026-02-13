package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class InnerPotentialPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = "basicmod:InnerPotentialPower";
    private final int hpLossPerTurn;

    public InnerPotentialPower(AbstractCreature owner, int hpLossPerTurn) {
        this.owner = owner;
        this.hpLossPerTurn = hpLossPerTurn;

        this.ID = POWER_ID;
        this.name = "内在潜力";
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.amount = 1; // 层数固定表示 Power 本身存在

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/InnerPotentialPower.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/InnerPotentialPower.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            addToBot(new LoseHPAction(owner, owner, hpLossPerTurn));
        }
    }

    @Override
    public void updateDescription() {
        this.description = "每回合失去 " + hpLossPerTurn + " 点生命。";
    }

    @Override
    public AbstractPower makeCopy() {
        return new InnerPotentialPower(owner, hpLossPerTurn);
    }
}
