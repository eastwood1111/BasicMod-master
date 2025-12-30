package basicmod.cards.skill;

import basicmod.Enums;
import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.CurrentStancePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import basicmod.BasicMod;

public class IronStanceCard extends BaseCard {
    public static final String ID = makeID(IronStanceCard.class.getSimpleName());
    private static final CardStrings cardStrings = com.megacrit.cardcrawl.core.CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );

    private static final int BASE_METAL = 6;
    private static final int UPG_METAL = 9;

    public IronStanceCard() {
        super(ID, info);
        this.name = NAME;
        this.baseMagicNumber = this.magicNumber = BASE_METAL;
        this.rawDescription = DESCRIPTION.replace("!METAL!", String.valueOf(BASE_METAL));

        this.selfRetain = true; // 回合结束保留
        this.tags.add(Enums.STANCE); // 标记为架势牌
        initializeDescription();
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CurrentStancePower stancePower = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);
        if (stancePower == null) {
            stancePower = new CurrentStancePower(p);
            p.addPower(stancePower);
        }

        // 切换到金属架势
        stancePower.switchStance(CurrentStancePower.Stance.IRON, this.magicNumber);

    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_METAL - BASE_METAL); // 9 → 12
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new IronStanceCard();
    }
}
