package basicmod.powers;

import basicmod.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class NoBlockPower extends AbstractPower {
    public static final String POWER_ID = "basicmod:NoBlockPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // 假设你有一个存放图片的路径，如果没有，可以使用原版图标代替
    // private static final Texture tex84 = TextureLoader.getTexture("basicmodResources/images/powers/NoBlock84.png");
    // private static final Texture tex32 = TextureLoader.getTexture("basicmodResources/images/powers/NoBlock32.png");

    public NoBlockPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1; // 这是一个状态型能力，不需要具体层数，设为-1
        this.type = PowerType.DEBUFF; // 虽然是自己施加的，但效果是负面的，通常归类为 Debuff

        // 如果没有自定义图片，借用原版“脆弱”或者“破甲”的图标
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );

        updateDescription();

    }

    /**
     * 核心逻辑：拦截格挡
     * 当生物获得格挡时调用。返回值为实际获得的格挡数。
     * 我们将其强制返回 0。
     */
    @Override
    public float modifyBlock(float blockAmount) {
        // 无论原本的格挡值是多少，我们都返回 0.0F
        // 这样就阻止了生物获得格挡
        return 0.0F;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}