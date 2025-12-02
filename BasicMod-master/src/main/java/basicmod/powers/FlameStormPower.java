package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class FlameStormPower extends AbstractPower implements CloneablePowerInterface {

    private final int hits;       // 随机伤害次数
    private final int baseDamage; // 原始伤害
    private final int ignite;     // 炎上层数

    public static final String POWER_ID = "basicmod:FlameStormPower";

    public FlameStormPower(AbstractCreature owner, int damage, int hits, int ignite) {
        this.owner = owner;
        this.baseDamage = damage;
        this.hits = hits;
        this.ignite = ignite;
        int adjustedDamage = baseDamage;
        if (owner.hasPower(MagicPower.POWER_ID)) {
            adjustedDamage += owner.getPower(MagicPower.POWER_ID).amount;
        }
        this.amount = adjustedDamage;
        this.ID = POWER_ID;
        this.name = "烈焰风暴";
        this.type = PowerType.BUFF;
        this.isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );

        updateAmount();       // 计算显示层数
        updateDescription();  // 更新描述
    }

    // 每次显示或计算伤害前调用
    private void updateAmount() {
        int finalDamage = baseDamage;
        if (owner.hasPower(MagicPower.POWER_ID)) {
            finalDamage += owner.getPower(MagicPower.POWER_ID).amount; // 叠加智力
        }
        this.amount = finalDamage; // Power 层数 = 最终伤害
    }

    @Override
    public void atStartOfTurn() {
        updateAmount(); // 确保伤害和显示正确

        for (int i = 0; i < hits; i++) {
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(true);
            if (target == null) break;

            // 随机造成伤害
            addToBot(new DamageAction(
                    target,
                    new DamageInfo(owner, this.amount, DamageInfo.DamageType.THORNS),
                    AbstractGameAction.AttackEffect.FIRE
            ));

            // 炎上效果
            if (ignite > 0) {
                addToBot(new ApplyPowerAction(
                        target,
                        owner,
                        new FlamePower(target, owner, ignite),
                        ignite
                ));
            }
        }

        // Power 触发后消失
        addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public void updateDescription() {
        updateAmount(); // 保证显示层数 = baseDamage + 智力
        if (ignite > 0) {
            this.description = "下回合随机对敌人造成 " + amount + " 点伤害 " + hits + " 次，每次赋予 " + ignite + " 层炎上。";
        } else {
            this.description = "下回合随机对敌人造成 " + amount + " 点伤害 " + hits + " 次。";
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new FlameStormPower(owner, baseDamage, hits, ignite);
    }
}
