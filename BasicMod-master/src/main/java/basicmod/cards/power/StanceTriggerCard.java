package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.StanceDrawPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

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
            1 // 基础费用
    );

    public StanceTriggerCard() {
        super(ID, info);
        this.name = NAME;
        this.rawDescription = DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        StanceDrawPower power = (StanceDrawPower) p.getPower(StanceDrawPower.POWER_ID);

        if (power == null) {
            // 玩家没有该 Power，添加一个初始层数为1的 Power
            p.addPower(new StanceDrawPower(p));
        } else {
            // 已经存在 Power，则叠加一层
            power.stackPower(1);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(this.cost - 1); // 升级后减1费
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StanceTriggerCard();
    }
}
