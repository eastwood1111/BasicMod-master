package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class SpearPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:SpearPower";
    public static final String NAME = "矛";

    // 修改描述：不再包含伤害数值，只描述移除格挡的效果
    public static final String[] DESCRIPTIONS = {"每次打出指向性攻击牌时，先移除目标的格挡。"};

    public SpearPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount; // 虽然这个能力现在不需要数值叠加产生效果，但保留 amount 是个好习惯，或者可以用来限制每回合触发次数
        this.type = PowerType.BUFF;

        // 资源路径建议根据你的项目实际情况修改
        // 注意：请确保这些路径下的图片文件真实存在，否则游戏会崩溃
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32);

        updateDescription();
    }

    // 使用 onPlayCard 在卡牌实际生效前插入动作
    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        // 检查是否是攻击牌且有明确的目标 (指向性单体攻击)
        if (card.type == AbstractCard.CardType.ATTACK && m != null) {

            // 只有当目标确实有格挡时才触发闪烁和移除动作，避免无意义的动画
            if (m.currentBlock > 0) {
                this.flash(); // 闪烁一下，表示能力触发

                // 移除格挡
                // 使用 addToBot 确保这个动作加入到动作队列中。
                // 由于 Slay the Spire 的动作队列机制，onPlayCard 触发时，卡牌本身的伤害动作通常已经在队列里了。
                // 为了确保"先移除格挡"，我们需要让这个动作插队到前面，或者确保它比伤害先结算。
                // 实际上，onPlayCard 发生在卡牌 use() 方法被调用之前（但在队列建立之后）。
                // 为了保险起见，让移除格挡发生在卡牌伤害之前，通常 addToBot 在 onPlayCard 里是安全的，
                // 因为卡牌的 use() 逻辑也是 addToBot，而 onPlayCard 在 use() 之前执行。
                this.addToBot(new LoseBlockAction(m, this.owner, m.currentBlock));
            }
        }
    }

    @Override
    public void updateDescription() {
        // 更新描述，不再需要格式化数字
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new SpearPower(owner, amount);
    }
}