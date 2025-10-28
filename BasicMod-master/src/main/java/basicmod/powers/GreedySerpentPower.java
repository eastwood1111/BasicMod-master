package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class GreedySerpentPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:GreedySerpentPower";
    public static final String NAME = "贪婪金蛇戒指";
    public static final String[] DESCRIPTIONS = new String[]{
            "掉落率增加"
    };

    public GreedySerpentPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
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
    public void onVictory() {
        AbstractRoom room = AbstractDungeon.getCurrRoom();

        // 只在精英房间生效（BossRoom 不加）
        if (room instanceof MonsterRoomElite) {
            for (int i = 0; i < amount; i++) {
                // 生成一个随机遗物
                AbstractRelic.RelicTier tier = AbstractDungeon.returnRandomRelicTier();
                AbstractRelic relic = AbstractDungeon.returnRandomRelic(tier);
                if (relic != null) {
                    room.addRelicToRewards(relic);
                }
            }
            addToTop(new RelicAboveCreatureAction(owner, AbstractDungeon.returnRandomRelic(AbstractRelic.RelicTier.COMMON)));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new GreedySerpentPower(owner, amount);
    }
}
