package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class FlamePower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = "soul-five:FlamePower";
    public static final String NAME = "炎上";
    public static final String[] DESCRIPTIONS = new String[]{
            "每回合开始时受到伤害，然后移除。"
    };

    public FlamePower(AbstractCreature owner, AbstractCreature source, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/FlamePower.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/FlamePower.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    // 回合开始时触发
    @Override
    public void atStartOfTurn() {
        // 造成伤害
        int damageAmount = this.amount; // 或者可根据你需求计算实际伤害
        DamageInfo dmgInfo = new DamageInfo(this.owner, damageAmount, DamageInfo.DamageType.THORNS);
        addToBot(new DamageAction(this.owner, dmgInfo));

        // 伤害后移除自身
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + "（" + this.amount + "层）";
    }

    @Override
    public AbstractPower makeCopy() {
        return new FlamePower(owner, null, amount);
    }
}
