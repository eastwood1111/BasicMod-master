package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.MagicPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class PowerToIntellectCard extends BaseCard {
    // Unique ID
    public static final String ID = "basicmod:PowerToIntellectCard";

    // Localization
    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    // Card Stats
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,  // Card Color
            CardType.SKILL,               // Card Type
            CardRarity.UNCOMMON,              // Rarity
            CardTarget.SELF,              // Target
            1                             // Cost
    );

    public PowerToIntellectCard() {
        super(ID, info);

        // Set basic properties
        // Note: Do not set this.name or this.rawDescription manually here if BaseCard handles it,
        // but if BaseCard doesn't auto-load strings, this is fine.

        // Define the values for "Magic" (Intellect gain) and Strength loss.
        // Since both values are 1 (or 2 when upgraded), we can use one variable.
        this.baseMagicNumber = 1;
        this.magicNumber = this.baseMagicNumber;

        this.selfRetain = false;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. Lose Strength (Apply negative Strength)
        // We use ApplyPowerAction with a negative amount.
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, -this.magicNumber), -this.magicNumber));

        // 2. Gain Intellect (MagicPower)
        addToBot(new ApplyPowerAction(p, p, new MagicPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // Upgrade the values from 1 to 2
            upgradeMagicNumber(1);

            // If your JSON description uses !M!, you don't need to change the description string manually.
            // Example JSON: "Lose !M! Strength. Gain !M! Intellect."
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new PowerToIntellectCard();
    }
}