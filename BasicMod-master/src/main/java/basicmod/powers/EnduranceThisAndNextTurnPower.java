package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class EnduranceThisAndNextTurnPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "soul-five:EnduranceThisAndNextTurnPower";
    public static final String NAME = "忍耐";

    public EnduranceThisAndNextTurnPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount; // 1 层代表：保护本回合 + 保护接下来的 1 个敌人回合
        this.type = PowerType.BUFF;
        this.isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        // 只要层数大于 0，受到的所有普通攻击伤害减半
        if (this.amount > 0 && type == DamageInfo.DamageType.NORMAL) {
            return damage * 0.5f;
        }
        return damage;
    }

    // --- 实现层数叠加的关键 ---
    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0f;
        this.amount += stackAmount; // 增加层数（即增加持续回合）
        updateDescription();
    }

    // 在你的下个回合开始抽牌后，层数减 1
    @Override
    public void atStartOfTurnPostDraw() {
        if (this.amount <= 1) {
            // 如果只剩 1 层，说明保护期限已到，移除能力
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            // 如果有多层，则减去 1 层
            addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = "本回合及下个敌人回合，受到的攻击伤害减半。";
        } else {
            this.description = "接下来的 #b" + this.amount + " 个回合内，受到的攻击伤害减半。";
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new EnduranceThisAndNextTurnPower(owner, amount);
    }
}