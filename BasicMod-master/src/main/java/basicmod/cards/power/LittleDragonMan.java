package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.NoBlockPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class LittleDragonMan extends BaseCard {
    public static final String ID = "basicmod:LittleDragonMan";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0 // 0费
    );

    public LittleDragonMan() {
        super(ID, info);

        // 获得 5 点力量
        this.baseMagicNumber = 5;
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 给予力量
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));

        // 2. 给予“无法获得格挡”的负面状态
        addToBot(new ApplyPowerAction(p, p, new NoBlockPower(p)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级后获得保留
            this.selfRetain = true;

            // 更新描述以显示“保留”
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LittleDragonMan();
    }
}