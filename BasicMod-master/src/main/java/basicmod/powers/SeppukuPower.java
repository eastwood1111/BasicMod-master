package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SeppukuPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "soul-five:SeppukuPower";
    public static final String NAME = "切腹";
    public static final String[] DESCRIPTIONS = {"当你造成伤害时，对敌人施加出血，触发次数等于层数"};

    public SeppukuPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/SeppukuPower.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/SeppukuPower.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    // 监听玩家造成伤害
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        // 只对攻击造成的伤害应用
        if (!(target instanceof AbstractMonster)) return;

        // 确保是攻击牌，并且伤害没有被格挡
        if (info.type != DamageInfo.DamageType.NORMAL || damageAmount <= 0 || info.output == 0) return;

        AbstractMonster m = (AbstractMonster) target;

        // 只有没有格挡的伤害才会触发出血
        if (m.getPower("Block") == null || m.getPower("Block").amount == 0) {
            // 每层出血
            for (int i = 0; i < this.amount; i++) {
                addToBot(new ApplyPowerAction(m, owner, new BleedPower(m, 1)));
            }
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + " 当前层数: " + this.amount;
    }

    @Override
    public AbstractPower makeCopy() {
        return new SeppukuPower(owner, amount);
    }
}
