package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FlameStormPower extends AbstractPower implements CloneablePowerInterface {

    private final String uniqueID;

    private final int hits;       // 随机伤害次数
    private final int baseDamage; // 永远保存基础伤害
    private final int ignite;     // 炎上层数

    public FlameStormPower(AbstractCreature owner, int baseDamage, int hits, int ignite, String uniqueID) {
        this.owner = owner;
        this.baseDamage = baseDamage;
        this.hits = hits;
        this.ignite = ignite;
        this.uniqueID = uniqueID;

        this.ID = "soul-five:FlameStormPower_" + uniqueID;
        this.name = "烈焰风暴";
        this.type = PowerType.BUFF;
        this.isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/FlameStorm.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/FlameStorm.png"), 0, 0, 32, 32
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

        // 描述跟着刷新（和阳光枪一样在这里直接写死也最稳）
        if (ignite > 0) {
            this.description = "下回合随机对敌人造成 " + this.amount + " 点伤害 " + hits + " 次，每次赋予 " + ignite + " 层炎上。";
        } else {
            this.description = "下回合随机对敌人造成 " + this.amount + " 点伤害 " + hits + " 次。";
        }
    }

    // ================================
    // 回合开始触发伤害
    // ================================
    @Override
    public void atStartOfTurn() {
        addToBot(new WaitAction(0.1f));

        int finalDamage = this.amount; // 已经是最终伤害（基础+智力）

        for (int i = 0; i < hits; i++) {
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(true);
            if (target == null) break;

            addToBot(new DamageAction(
                    target,
                    new DamageInfo(owner, finalDamage, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.FIRE
            ));

            if (ignite > 0) {
                addToBot(new ApplyPowerAction(
                        target,
                        owner,
                        new FlamePower(target, owner, ignite),
                        ignite
                ));
            }
        }

        // 触发后移除自己（用唯一ID）
        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public AbstractPower makeCopy() {
        return new FlameStormPower(owner, this.baseDamage, this.hits, this.ignite, this.uniqueID);
    }
}
