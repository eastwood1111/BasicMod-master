package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class DoubleNextAttackPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:DoubleNextAttackPower";
    public static final String NAME = "准备";
    public static final String[] DESCRIPTIONS = {"下一张攻击牌造成双倍伤害。"};

    public DoubleNextAttackPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // 设置 Power 图片，替换成你自己的资源路径
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    // 触发攻击牌伤害翻倍
    @Override
    public float atDamageGive(float damage, com.megacrit.cardcrawl.cards.DamageInfo.DamageType type) {
        if (type == com.megacrit.cardcrawl.cards.DamageInfo.DamageType.NORMAL) {
            return damage * 2;
        }
        return damage;
    }

    // 使用攻击牌后移除 Power
    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(owner, owner, this)
            );
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DoubleNextAttackPower(owner);
    }
}
