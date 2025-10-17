package basicmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class AgilityAuraPower extends AbstractPower {
    public static final String POWER_ID = "basicmod:AgilityAuraPower";

    private int dexAmount;

    public AgilityAuraPower(AbstractCreature owner, int dexAmount) {
        this.name = "Agility Aura";
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = dexAmount;
        this.dexAmount = dexAmount;
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
                    new ApplyPowerAction(owner, owner, new DexterityPower(owner, dexAmount), dexAmount)
            );
        }
    }

    @Override
    public void onRemove() {
        // 安全移除时不会产生副作用
    }

    @Override
    public void updateDescription() {
        this.description = "每回合开始时，获得 " + dexAmount + " 层敏捷。";
    }
}
