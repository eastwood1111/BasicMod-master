package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;

public class StrategicReserve extends BaseCard {
    public static final String ID = "basicmod:StrategicReserve";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON, // 罕见
            CardTarget.SELF,
            1 // 1费
    );

    public StrategicReserve() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;

        // 基础格挡值设置为3 (这里用 magicNumber 来存储每张牌的格挡量)
        this.baseMagicNumber = this.magicNumber = 3;

        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 调用自定义动作，传入当前的 magicNumber (3 或 4)
        addToBot(new StrategicReserveAction(this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1); // 升级后增加1点 (3 -> 4)
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StrategicReserve();
    }

    // --- 内部类：自定义动作 ---
    // 你也可以把这个类单独存为一个 .java 文件，为了方便复制我写在内部了
    public static class StrategicReserveAction extends AbstractGameAction {
        private int blockPerCard;

        public StrategicReserveAction(int blockPerCard) {
            this.blockPerCard = blockPerCard;
            this.actionType = ActionType.CARD_MANIPULATION;
            this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        }

        @Override
        public void update() {
            // 步骤1：动作开始，打开选择界面
            if (this.duration == this.startDuration) {
                if (AbstractDungeon.player.hand.isEmpty()) {
                    this.isDone = true;
                    return;
                }

                // 参数说明: (提示文本, 最大数量, 是否允许任意数量, 是否允许选0张)
                // TEXT,[object Object], 需要你在本地化文件中配置，例如 "选择放入抽牌堆的牌"
                String selectMsg = "选择放入抽牌堆的牌";
                AbstractDungeon.handCardSelectScreen.open(selectMsg, 99, true, true);

                this.tickDuration();
                return;
            }

            // 步骤2：玩家选完牌后，处理逻辑
            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                int count = 0;

                // 遍历玩家选择的牌
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    // 将牌放回抽牌堆 (addToRandomSpot 是洗入，addToTop 是放顶端，通常"放入"指洗入)
                    AbstractDungeon.player.drawPile.addToRandomSpot(c);
                    // 视觉效果处理
                    c.unhover();
                    c.lighten(true);
                    count++;
                }

                // 清空选择列表，防止逻辑重复
                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();

                // 如果选了牌，获得格挡
                if (count > 0) {
                    // 获得总格挡 = 张数 * 每张格挡值
                    addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, count * blockPerCard));
                }
            }

            this.tickDuration();
        }
    }
}