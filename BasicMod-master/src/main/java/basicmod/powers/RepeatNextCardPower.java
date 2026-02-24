package basicmod.powers;

import basicmod.util.TextureLoader; // 假设你有这个工具类加载图片
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RepeatNextCardPower extends AbstractPower {
    public static final String POWER_ID = "soul-five:RepeatNextCardPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // 假设你的图片路径

    public RepeatNextCardPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF; // 这是一个增益效果
        this.isTurnBased = false;   // 不是回合结束自动消失，而是消耗型

        // 加载图片
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
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0]; // "你打出的下一张0费卡将重复打出1次。"
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2]; // "你打出的下 #b" + amount + " 张0费卡将重复打出1次。"
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        // 核心逻辑：
        // 1. 必须不是被清除状态 (purgeOnUse)
        // 2. 根据你的卡牌描述，必须是 0 费卡 (card.costForTurn == 0)
        // 3. 必须不是这张 Power 自身产生的重复效果（通常通过 amount > 0 判定，但这里是在打出时触发）

        if (!card.purgeOnUse && card.costForTurn == 0 && this.amount > 0) {

            // 闪烁效果提示玩家触发了
            this.flash();

            // 创建一张临时卡牌用于重复打出
            AbstractCard tmp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (float)Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float)Settings.HEIGHT / 2.0F;

            // 重新计算卡牌属性（针对当前目标）
            if (tmp.cost > 0) {
                tmp.freeToPlayOnce = true; // 确保重复打出时不耗费能量
            }

            // 获取目标怪物
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster)action.target;
            }

            // 只有当卡牌不需要目标，或者目标怪物还没死的时候才重复打出
            if (m == null || !m.isDeadOrEscaped()) {
                // purgeOnUse = true 确保这张临时卡打完后消失，不进入弃牌堆
                tmp.purgeOnUse = true;

                // 将卡牌加入打出队列
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
            }

            // 减少层数
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 如果是玩家的回合结束，移除这个 Power
        if (isPlayer) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
    }
}