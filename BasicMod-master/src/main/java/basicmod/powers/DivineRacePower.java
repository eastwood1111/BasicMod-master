package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DivineRacePower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "soul-five:DivineRacePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public static final String NAME = "神族入侵";
    public static final String[] DESCRIPTIONS = {
            "本回合还能打出 #b",
            " 张牌。下个回合开始时你将被遣返（死亡）。",
            " #r你已被遣返，无法行动，等待死亡。"
    };

    public DivineRacePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF; // 药丸洗不掉
        this.isTurnBased = false;
        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );
        updateDescription();
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        if (this.amount <= 0) {
            card.cantUseMessage = "你已被神族遣返";
            return false;
        }
        return super.canPlayCard(card);
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (this.amount > 0) {
            this.amount -= 1;
            this.flash();
            updateDescription();
        }
    }

    @Override
    public void atStartOfTurn() {
        // 核心逻辑：无论 amount 是多少，只要到下个回合，直接死亡
        this.flash();
        addToBot(new LoseHPAction(owner, owner, 99999));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void updateDescription() {
        if (this.amount > 0) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new DivineRacePower(owner, amount);
    }
}