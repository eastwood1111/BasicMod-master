package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class BleedPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:BleedPower";
    public static final String NAME = "出血";
    public static final String[] DESCRIPTIONS = {"每回合开始失去层数 × 3 点生命。"};

    public BleedPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/BleedPower.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/BleedPower.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    // 每回合开始触发
    @Override
    public void atStartOfTurn() {
        if (this.amount > 0) {
            // 每层造成 3 点失去生命
            int loseHp = this.amount * 3;
            addToBot(new com.megacrit.cardcrawl.actions.common.LoseHPAction(
                    this.owner,
                    this.owner,
                    loseHp
            ));

            // 每回合减少层数
            addToBot(new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
    @Override
    public void onRemove() {
        // 玩家拥有 BloodKingPleasurePower 时触发
        AbstractCreature player = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
        if (player.hasPower("basicmod:BloodKingPleasurePower")) {
            BloodKingPleasurePower power = (BloodKingPleasurePower) player.getPower("basicmod:BloodKingPleasurePower");
            power.onEnemyBleedRemoved(this.owner);
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
        return new BleedPower(owner, amount);
    }
}
