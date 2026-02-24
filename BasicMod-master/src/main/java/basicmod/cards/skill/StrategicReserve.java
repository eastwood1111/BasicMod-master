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
    public static final String ID = makeID(StrategicReserve.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    public StrategicReserve() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;

        // --- 修改点 1: 使用 baseBlock 代替 magicNumber ---
        this.baseBlock = 3;

        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // --- 修改点 2: 传入 this.block (这是经过敏捷加成计算后的最终值) ---
        addToBot(new StrategicReserveAction(this.block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // --- 修改点 3: 升级 baseBlock ---
            upgradeBlock(1);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StrategicReserve();
    }

    // --- 内部类：自定义动作 ---
    public static class StrategicReserveAction extends AbstractGameAction {
        private int blockPerCard;

        public StrategicReserveAction(int blockPerCard) {
            this.blockPerCard = blockPerCard;
            this.actionType = ActionType.CARD_MANIPULATION;
            this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        }

        @Override
        public void update() {
            if (this.duration == this.startDuration) {
                if (AbstractDungeon.player.hand.isEmpty()) {
                    this.isDone = true;
                    return;
                }

                String selectMsg = "选择放入抽牌堆的牌";
                // 允许选任意张 (1到99张)
                AbstractDungeon.handCardSelectScreen.open(selectMsg, 99, true, true);
                this.tickDuration();
                return;
            }

            if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
                int count = AbstractDungeon.handCardSelectScreen.selectedCards.size();

                if (count > 0) {
                    for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                        AbstractDungeon.player.drawPile.addToRandomSpot(c);
                    }

                    // --- 关键逻辑：获得 (敏捷加成后的单张格挡 * 张数) 的格挡 ---
                    addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, count * blockPerCard));
                }

                AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
                AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            }

            this.tickDuration();
        }
    }
}