package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class CurseDeathPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = "basicmod:CurseDeathPower";
    public static final String NAME = "咒死";
    public static final String[] DESCRIPTIONS = {
            "叠满10层后将受到等同于生命上限的伤害，然后移除所有咒死。每回合开始减少1层。"
    };

    public CurseDeathPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;

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
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);

        // 检查是否达到10层
        if (this.amount >= 10) {
            int maxHpDamage = owner.maxHealth;

            // 直接扣除最大生命上限的伤害（等同于直接死亡）
            AbstractDungeon.actionManager.addToBottom(
                    new LoseHPAction(owner, owner, maxHpDamage)
            );

            // 移除全部咒死
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(owner, owner, POWER_ID)
            );
        }
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount > 0) {
            this.amount--;

            // 层数减少到 0 自动移除
            if (this.amount <= 0) {
                AbstractDungeon.actionManager.addToBottom(
                        new RemoveSpecificPowerAction(owner, owner, POWER_ID)
                );
            }
        }
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + "（当前 " + this.amount + " 层）";
    }

    @Override
    public AbstractPower makeCopy() {
        return new CurseDeathPower(owner, amount);
    }
}
