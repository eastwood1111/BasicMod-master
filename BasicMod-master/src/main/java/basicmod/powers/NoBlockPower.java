package basicmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class NoBlockPower extends AbstractPower {
    public static final String POWER_ID = "basicmod:NoBlockPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;

    // 你也可以改成从 language json 读，但这里先保持你原来的写法，方便测试
    public static final String[] DESCRIPTIONS = {"你无法从卡牌内获得格挡 "};

    public NoBlockPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;

        // 状态型能力不需要层数
        this.amount = -1;

        this.type = PowerType.DEBUFF;

        // 图标（按你原来的路径）
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/NoBlockPower.png"),
                0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/NoBlockPower.png"),
                0, 0, 32, 32
        );

        // 可选：提高优先级（并非必须，但有时能避免模组里奇怪的顺序问题）

        updateDescription();
    }

    /**
     * 第一步：把当前计算中的 block 直接归零
     * 注意：敏捷等 Power 可能会在后续流程里又把 block 加回去
     */
    // 在 NoBlockPower 类中

    @Override
    public float modifyBlock(float blockAmount) {
        return 0.0F;
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        // 明确针对卡牌显示时返回 0
        return 0.0F;
    }


    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
