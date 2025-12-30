package basicmod.relics;

import basicmod.Enums;
import basicmod.charater.MyCharacter;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static basicmod.BasicMod.makeID;

public class MyRelicUpgrade extends BaseRelic {
    // 必须有唯一的 ID，比如 MyRelicUpgrade
    public static final String ID = makeID("MyRelicUpgrade");
    private boolean triggeredThisTurn = false;

    public MyRelicUpgrade() {
        super(ID, "myrelic", MyCharacter.Meta.CARD_COLOR, RelicTier.BOSS, LandingSound.MAGICAL);
    }

    // --- 核心逻辑 1：只有拥有初始遗物时，BOSS 掉落池才会出现这个遗物 ---
    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(MyRelic.ID);
    }

    // --- 核心逻辑 2：当玩家拾取该遗物时，替换掉初始遗物 ---
    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(MyRelic.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(MyRelic.ID)) {
                    // 在原有的位置替换，参数：玩家, 索引位置, 是否触发拾取效果
                    this.instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    // 抽牌逻辑（抽 2 张）
    @Override
    public void onUseCard(AbstractCard card, com.megacrit.cardcrawl.actions.utility.UseCardAction action) {
        if (!this.triggeredThisTurn && card.hasTag(Enums.STANCE)) {
            this.triggeredThisTurn = true;
            this.flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new DrawCardAction(AbstractDungeon.player, 2)); // 升级版抽 2 张
            this.grayscale = true;
        }
    }

    @Override
    public void atTurnStart() {
        this.triggeredThisTurn = false;
        this.grayscale = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // 对应 RelicStrings.json 中 MyRelicUpgrade 的描述
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MyRelicUpgrade();
    }
}