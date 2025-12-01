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

public class MeteorNextTurnPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = "basicmod:MeteorNextTurnPower";
    private final int hitTimes;
    private final int baseDamage;

    public MeteorNextTurnPower(AbstractCreature owner, int damage, int hitTimes) {
        this.owner = owner;
        this.baseDamage = damage; // 保存基础伤害，不受力量
        this.amount = damage; // Power 层数初始为基础伤害
        this.hitTimes = hitTimes;

        this.ID = POWER_ID;
        this.name = "毁灭流星蓄力";
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
        int finalDamage = baseDamage;
        if (owner.hasPower(MagicPower.POWER_ID)) {
            finalDamage += owner.getPower(MagicPower.POWER_ID).amount;
        }

        // 层数显示 = 最终伤害
        this.amount = finalDamage;

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
        this.description = "下回合对所有敌人造成 " + amount + " 点伤害，共 " + hitTimes + " 次（智力加成已计入）。";
    }

    @Override
    public AbstractPower makeCopy() {
        return new MeteorNextTurnPower(owner, baseDamage, hitTimes);
    }
}
