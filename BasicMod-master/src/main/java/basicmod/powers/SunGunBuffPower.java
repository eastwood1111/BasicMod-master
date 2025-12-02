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
    private final String uniqueID;

    public SunGunBuffPower(AbstractCreature owner, int baseDamage, String uniqueID) {
        this.owner = owner;
        this.amount = baseDamage; // 基础伤害
        this.uniqueID = uniqueID;
        int adjustedDamage = baseDamage;
        if (owner.hasPower(MagicPower.POWER_ID)) {
            adjustedDamage += owner.getPower(MagicPower.POWER_ID).amount;
        }
        this.amount = adjustedDamage;
        this.ID = "basicmod:SunGunBuffPower_" + uniqueID; // 独立 ID
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
        addToBot(new WaitAction(0.1f));

        // 计算最终伤害 = baseDamage + 智力
        int finalDamage = this.amount; // this.amount = baseDamage
        if (owner.hasPower(MagicPower.POWER_ID)) {
            finalDamage += owner.getPower(MagicPower.POWER_ID).amount;
        }

        addToBot(new DamageRandomEnemyAction(
                new DamageInfo(owner, finalDamage, DamageInfo.DamageType.THORNS),
                AbstractGameAction.AttackEffect.FIRE
        ));

        // 层数显示 = 最终伤害
        this.amount = finalDamage;

        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public void updateDescription() {
        this.description = "下回合对随机敌人造成 " + amount + " 点伤害，额外增加智力加成。";
    }

    @Override
    public AbstractPower makeCopy() {
        return new SunGunBuffPower(owner, amount, uniqueID);
    }
}
