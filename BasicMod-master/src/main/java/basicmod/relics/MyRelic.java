package basicmod.relics;

import basicmod.cards.skill.PageFragment;
import basicmod.charater.MyCharacter;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.BasicMod.makeID;

public class MyRelic extends BaseRelic {
    public static final String ID = makeID("MyRelic");

    private static final int BASE_COUNT = 1;
    private static final int UPGRADE_COUNT = 2;
    private final boolean upgraded;

    // 构造函数
    public MyRelic() {
        this(false);
    }

    public MyRelic(boolean upgraded) {
        super(ID, "CodexRelic", MyCharacter.Meta.CARD_COLOR,
                upgraded ? RelicTier.BOSS : RelicTier.STARTER,
                LandingSound.MAGICAL);
        this.upgraded = upgraded;
        this.grayscale = !upgraded; // 升级版不灰色
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        int count = upgraded ? UPGRADE_COUNT : BASE_COUNT;
        for (int i = 0; i < count; i++) {
            AbstractDungeon.actionManager.addToBottom(
                    new MakeTempCardInHandAction(new PageFragment(), 1)
            );
        }
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
