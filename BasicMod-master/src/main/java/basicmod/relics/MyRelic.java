package basicmod.relics;

import basicmod.Enums;
import basicmod.charater.MyCharacter;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.BasicMod.makeID;

public class MyRelic extends BaseRelic {
    public static final String ID = makeID("MyRelic");
    private boolean triggeredThisTurn = false;

    public MyRelic() {
        super(ID, "myrelic", MyCharacter.Meta.CARD_COLOR, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void atTurnStart() {
        this.triggeredThisTurn = false;
        this.grayscale = false;
    }

    @Override
    public void onUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        // 初始版：检测架势标签，抽 1 张
        if (!this.triggeredThisTurn && card.hasTag(Enums.STANCE)) {
            this.triggeredThisTurn = true;
            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new DrawCardAction(AbstractDungeon.player, 1));
            this.grayscale = true;
        }
    }

    @Override
    public void onVictory() {
        this.grayscale = false;
        this.triggeredThisTurn = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MyRelic();
    }
}