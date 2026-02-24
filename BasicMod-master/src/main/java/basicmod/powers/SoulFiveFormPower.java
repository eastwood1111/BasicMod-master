package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class SoulFiveFormPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "soul-five:SoulFiveFormPower";
    public static final String NAME = "SoulFive形态";
    public static final String[] DESCRIPTIONS = new String[]{
            "每回合抽牌和能量等于层数，每回合结束后失去层数并对自己造成层数×2点伤害。"
    };

    public SoulFiveFormPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount; // 层数
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/SoulFiveFormPower.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/SoulFiveFormPower.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (owner instanceof AbstractPlayer) {
            AbstractPlayer p = (AbstractPlayer) owner;
            if (amount > 0) {
                addToBot(new DrawCardAction(p, amount));
                addToBot(new GainEnergyAction(amount));
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (amount > 0) {
            int loseHp = this.amount * 2; // 层数乘2
            addToBot(new com.megacrit.cardcrawl.actions.common.LoseHPAction(owner, owner, loseHp));
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + " 当前层数: " + this.amount;
    }

    @Override
    public AbstractPower makeCopy() {
        return new SoulFiveFormPower(owner, amount);
    }
}
