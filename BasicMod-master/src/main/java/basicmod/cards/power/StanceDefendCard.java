package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.StanceDefendPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

public class StanceDefendCard extends BaseCard {
    public static final String ID = makeID(StanceDefendCard.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int BASE_STACK = 3;
    private static final int UPG_STACK = 1; // 增加的数值

    public StanceDefendCard() {
        super(ID, info);

        // 建议：使用 baseMagicNumber 配合 JSON 中的 !M!
        this.baseMagicNumber = this.magicNumber = BASE_STACK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用 ApplyPowerAction 会自动调用 Power 里的 stackPower
        // 这样如果你打出两张此卡，每次进入架势就会获得 3+3=6 点格挡
        addToBot(new ApplyPowerAction(p, p, new StanceDefendPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级建议：增加格挡值
            upgradeMagicNumber(UPG_STACK);
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StanceDefendCard();
    }
}