package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import basicmod.powers.MagicPower;

public class CometAzrePower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = "basicmod:CometAzreNextTurnPower";
    private final int hitTimes;
    private final int baseDamage;

    public CometAzrePower(AbstractCreature owner, int damage, int hitTimes) {
        this.owner = owner;
        this.baseDamage = damage; // 基础伤害（卡牌已经处理魔法架势加成）
        this.amount = damage; // 初始层数显示为基础伤害
        this.hitTimes = hitTimes;
        int adjustedDamage = baseDamage;
        if (owner.hasPower(MagicPower.POWER_ID)) {
            adjustedDamage += owner.getPower(MagicPower.POWER_ID).amount;
        }
        this.amount = adjustedDamage; // 层数显示包含智力
        this.ID = POWER_ID;
        this.name = "彗星亚兹勒";
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

        // 计算最终伤害 = 卡牌决定的基础伤害 + 智力加成
        int finalDamage = baseDamage;
        if (owner.hasPower(MagicPower.POWER_ID)) {
            finalDamage += owner.getPower(MagicPower.POWER_ID).amount;
        }

        // 更新层数显示为最终伤害
        this.amount = finalDamage;

        // 对所有敌人造成多次伤害
        for (int i = 0; i < hitTimes; i++) {
            addToBot(new DamageAllEnemiesAction(
                    owner,
                    DamageInfo.createDamageMatrix(finalDamage, true),
                    DamageInfo.DamageType.THORNS, // 不受力量影响
                    AbstractGameAction.AttackEffect.FIRE
            ));
        }

        // 触发后移除 Power
        addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
    }

    @Override
    public void updateDescription() {
        this.description = "下回合对所有敌人造成 " + amount + " 点伤害，共 " + hitTimes + " 次";
    }

    @Override
    public AbstractPower makeCopy() {
        return new CometAzrePower(owner, baseDamage, hitTimes);
    }
}
