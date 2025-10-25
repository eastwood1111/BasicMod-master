package basicmod.relics;

import basicmod.cards.skill.SwordStanceCard;
import basicmod.cards.skill.ShieldStanceCard;
import basicmod.charater.MyCharacter;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.BasicMod.makeID;

public class MyRelic extends BaseRelic {
    public static final String ID = makeID("MyRelic");

    private final boolean upgraded;

    // 构造函数
    public MyRelic() {
        this(false);
    }

    public MyRelic(boolean upgraded) {
        super(ID, "myrelic", MyCharacter.Meta.CARD_COLOR,
                upgraded ? RelicTier.BOSS : RelicTier.STARTER,
                LandingSound.MAGICAL);
        this.upgraded = upgraded;
        this.grayscale = !upgraded; // 升级版不灰色
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        // 开局加入剑架势和盾架势牌
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInHandAction(new SwordStanceCard(), 1)
        );
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInHandAction(new ShieldStanceCard(), 1)
        );
    }

    @Override
    public String getUpdatedDescription() {
        return upgraded ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MyRelic(upgraded);
    }
}
