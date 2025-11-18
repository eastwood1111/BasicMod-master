package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class SunGunBuffPower extends AbstractPower implements CloneablePowerInterface {
    public String uniqueID;

    public SunGunBuffPower(AbstractCreature owner, int amount, String uniqueID) {
        this.owner = owner;
        this.amount = amount;
        this.uniqueID = uniqueID;

        this.ID = "basicmod:SunGunBuffPower_" + uniqueID; // 每个实例独立ID
        this.name = "阳光枪能量";
        this.type = PowerType.BUFF;
        this.isTurnBased = true;

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
        // 延迟伤害触发
        addToBot(new WaitAction(0.1f));
        addToBot(new DamageRandomEnemyAction(
                new DamageInfo(owner, this.amount, DamageInfo.DamageType.THORNS),
                AbstractGameAction.AttackEffect.FIRE
        ));

        // 触发后移除自身
        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public void updateDescription() {
        this.description = "下回合对随机敌人造成 " + amount + " 点伤害。";
    }

    @Override
    public AbstractPower makeCopy() {
        return new SunGunBuffPower(owner, amount, uniqueID);
    }
}
