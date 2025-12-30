package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.StanceDrawPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction; // 引入 Action
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class StanceTriggerCard extends BaseCard {
    public static final String ID = makeID(StanceTriggerCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    public StanceTriggerCard() {
        super(ID, info);
        this.name = NAME;
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用标准的 ApplyPowerAction
        // 它会自动判断：如果玩家没有这个 Power，就 new 一个；如果有，就调用 stackPower 叠加
        addToBot(new ApplyPowerAction(p, p, new StanceDrawPower(p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(0); // 1费变0费更直接的写法
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StanceTriggerCard();
    }
}