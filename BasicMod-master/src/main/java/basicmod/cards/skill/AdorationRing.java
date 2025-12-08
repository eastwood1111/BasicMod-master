package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class AdorationRing extends BaseCard {
    public static final String ID = "basicmod:AdorationRing";
    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0  // 0费
    );

    private static final int BASE_ENERGY = 2;
    private static final int UPG_ENERGY = 3;

    public AdorationRing() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;

        this.baseMagicNumber = this.magicNumber = BASE_ENERGY;

        this.exhaust = true; // 打出后消耗

        initializeDescription();
    }

    // 抽到时触发
    @Override
    public void triggerWhenDrawn() {
        addToBot(new GainEnergyAction(this.magicNumber));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 打出后额外效果可添加，这里暂时没有其他效果
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_ENERGY - BASE_ENERGY); // 升级增加能量
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new AdorationRing();
    }
}
