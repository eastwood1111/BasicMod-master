package basicmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DrawNextTurnPower extends AbstractPower {
    public static final String POWER_ID = "basicmod:DrawNextTurnPower";

    public DrawNextTurnPower(AbstractCreature owner, int drawAmount) {
        this.name = "下回合抽牌"; // 建议后续改为读取本地化文件
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = drawAmount;
        this.type = PowerType.BUFF;

        // 通常这类一次性Buff设为 true 看起来更像回合类状态，设为 false 也没问题
        this.isTurnBased = true;

        // 图片加载优化建议：不要在构造函数里直接 new TextureAtlas。
        // 如果你没有用 TextureLoader，暂时保持这样也可以，但建议检查路径是否完全正确。
        // 这里的 84, 84 和 32, 32 是标准大小
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
        this.description = "下回合开始时，抽 " + this.amount + " 张牌。";
    }

    @Override
    public void atStartOfTurn() {
        // 1. 播放闪烁动画，提示玩家该能力触发了
        this.flash();

        // 2. 执行抽牌
        addToBot(new DrawCardAction(this.owner, this.amount));

        // 3. 触发后移除自身
        // 不需要判断 owner 是否是 Player，怪物也有可能获得此能力，移除逻辑是一样的
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }
}