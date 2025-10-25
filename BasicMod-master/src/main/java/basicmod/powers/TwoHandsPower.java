package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import basicmod.BasicMod;

public class TwoHandsPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = BasicMod.makeID("TwoHandsPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public boolean canHoldTwoStances = true;

    public TwoHandsPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        // 使用统一纹理
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
        this.description = DESCRIPTIONS[0];
    }

    /** 判断是否拥有双架势能力 */
    public static boolean hasTwoHands(AbstractCreature owner) {
        return owner.hasPower(POWER_ID);
    }

    /** 你可以在 CurrentStancePower 中使用此方法允许同时存在两个架势 */



    @Override
    public AbstractPower makeCopy() {
        return new TwoHandsPower(owner);
    }
}
