package basicmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;

public class DrawNextTurnPower extends AbstractPower {
    public static final String POWER_ID = "basicmod:DrawNextTurnPower";

    public DrawNextTurnPower(AbstractCreature owner, int drawAmount) {
        this.name = "下回合抽牌";
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = drawAmount;  // 下回合要抽的卡牌数量
        this.type = PowerType.BUFF;
        this.isTurnBased = false;  // 让它在下回合触发
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = "下回合抽 " + this.amount + " 张牌";  // 更新描述
    }

    @Override
    public void atStartOfTurn() {
        // 在下回合开始时触发，抽取指定数量的卡牌
        addToBot(new DrawCardAction(this.owner, this.amount));  // 执行抽牌动作

        // 触发后移除该 Power
        if (this.owner instanceof AbstractPlayer) {
            AbstractPlayer p = (AbstractPlayer) this.owner;

            // 需要确保叠加效果
            AbstractPower existingPower = p.getPower(POWER_ID);
            if (existingPower != null) {
                // 如果已经有相同的 Power，累加数量
                existingPower.amount += this.amount;
            } else {
                // 如果没有该 Power，直接添加新的 Power
                addToBot(new RemoveSpecificPowerAction(p, p, this.ID));
            }
        }
    }
}
