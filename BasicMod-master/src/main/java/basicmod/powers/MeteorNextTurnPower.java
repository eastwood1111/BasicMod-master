package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class MeteorNextTurnPower extends AbstractPower implements CloneablePowerInterface {

    private final String uniqueID;

    private final int hitTimes;
    private final int baseDamage; // 永远保存基础伤害

    public MeteorNextTurnPower(AbstractCreature owner, int baseDamage, int hitTimes, String uniqueID) {
        this.owner = owner;
        this.baseDamage = baseDamage;
        this.hitTimes = hitTimes;
        this.uniqueID = uniqueID;

        // ✅ 独立ID，避免覆盖
        this.ID = "basicmod:MeteorNextTurnPower_" + uniqueID;

        this.name = "毁灭流星";
        this.type = PowerType.BUFF;
        this.isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/DestructionMeteorCard.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/DestructionMeteorCard.png"), 0, 0, 32, 32
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

        this.description = "下回合对所有敌人造成 " + this.amount + " 点伤害，共 " + hitTimes + " 次。";
    }

    // ================================
    // 回合开始触发
    // ================================
    @Override
    public void atStartOfTurn() {
        addToBot(new WaitAction(0.1f));

        int finalDamage = this.amount; // 已经是最终伤害（基础+智力）

        for (int i = 0; i < hitTimes; i++) {
            addToBot(new DamageAllEnemiesAction(
                    owner,
                    DamageInfo.createDamageMatrix(finalDamage, true),
                    DamageInfo.DamageType.THORNS, // 不受力量影响
                    AbstractGameAction.AttackEffect.FIRE
            ));
        }

        // ✅ 用 this.ID 移除自己（独立实例）
        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public AbstractPower makeCopy() {
        return new MeteorNextTurnPower(owner, this.baseDamage, this.hitTimes, this.uniqueID);
    }
}
