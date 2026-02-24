package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SunGunBuffPower extends AbstractPower implements CloneablePowerInterface {

    private final String uniqueID;
    private final int baseDamage; // 永远保存基础伤害

    public SunGunBuffPower(AbstractCreature owner, int baseDamage, String uniqueID) {
        this.owner = owner;
        this.baseDamage = baseDamage;
        this.uniqueID = uniqueID;

        this.ID = "basicmod:SunGunBuffPower_" + uniqueID;
        this.name = "阳光枪能量";
        this.type = PowerType.BUFF;
        this.isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/SunGun.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/SunGun.png"), 0, 0, 32, 32
        );

        updateDamageDisplay(); // 初始化显示
    }

    // ================================
    // 每帧刷新显示数值
    // ================================
    @Override
    public void update(int slot) {
        super.update(slot);
        updateDamageDisplay();
    }

    private void updateDamageDisplay() {
        int intel = 0;
        if (owner != null && owner.hasPower(MagicPower.POWER_ID)) {
            intel = owner.getPower(MagicPower.POWER_ID).amount;
        }

        // 显示最终伤害 = 基础 + 智力
        this.amount = baseDamage + intel;

        this.description = "下回合对随机敌人造成 " + this.amount + " 点伤害。";
    }

    // ================================
    // 回合开始触发伤害
    // ================================
    @Override
    public void atStartOfTurn() {
        addToBot(new WaitAction(0.1f));

        int finalDamage = this.amount; // 已经是最终伤害

        addToBot(new DamageRandomEnemyAction(
                new DamageInfo(owner, finalDamage, DamageInfo.DamageType.THORNS),
                AbstractGameAction.AttackEffect.FIRE
        ));

        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public AbstractPower makeCopy() {
        return new SunGunBuffPower(owner, this.baseDamage, this.uniqueID);
    }
}
