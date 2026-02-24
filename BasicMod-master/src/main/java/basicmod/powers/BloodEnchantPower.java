package basicmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BloodEnchantPower extends AbstractPower {
    public static final String POWER_ID = "basicmod:BloodEnchantPower";
    private boolean isTriggering = false; // 记录当前正在打出的卡牌是否应该触发效果

    public BloodEnchantPower(AbstractCreature owner, int amount) {
        this.name = "染血";
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/BleedPower.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/BleedPower.png"), 0, 0, 32, 32
        );// 借用涂毒图标
        updateDescription();
    }

    // 1. 当玩家开始使用卡牌时
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        // 如果是攻击牌，且还有剩余生效次数
        if (card.type == AbstractCard.CardType.ATTACK && this.amount > 0) {
            this.isTriggering = true; // 开启出血监听
        }
    }

    // 2. 核心逻辑：每造成一次伤害判定（多段攻击会触发多次）
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        // 必须是该 Buff 激活期间造成的【攻击伤害】，且目标不是自己
        if (this.isTriggering && info.type == DamageInfo.DamageType.NORMAL && target != this.owner) {
            this.flash();
            // 每次伤害赋予 1 层你写的 BleedPower（层数可根据需要调整）
            addToTop(new ApplyPowerAction(target, this.owner, new BleedPower(target, 1), 1));
        }
    }

    // 3. 当卡牌效果结算完毕
    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (this.isTriggering) {
            this.isTriggering = false; // 关闭监听
            this.amount--; // 减少剩余卡牌层数

            if (this.amount <= 0) {
                addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            }
            updateDescription();
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            // 回合结束强制消失
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }

    @Override
    public void updateDescription() {
        this.description = "你接下来的 " + this.amount + " 张攻击牌，每造成一次伤害都会施加 1 层出血。";
    }
}