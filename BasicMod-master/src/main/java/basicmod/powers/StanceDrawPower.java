package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class StanceDrawPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:StanceDrawPower";
    public static final String NAME = "架势抽牌";
    public static final String[] DESCRIPTIONS = { "每次进入架势时，抽 #b 张牌。" };

    public StanceDrawPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32);

        updateDescription();
    }

    // --- 关键修改：提供给外部调用的触发接口 ---
    public void onStanceTrigger() {
        this.flash(); // 图标闪烁
        // 根据 Power 的层数 (this.amount) 抽牌
        addToBot(new DrawCardAction(this.owner, this.amount));
    }

    @Override
    public void stackPower(int stackAmount) {
        this.amount += stackAmount;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("#b", String.valueOf(this.amount));
    }

    @Override
    public AbstractPower makeCopy() {
        return new StanceDrawPower(owner, amount);
    }
}