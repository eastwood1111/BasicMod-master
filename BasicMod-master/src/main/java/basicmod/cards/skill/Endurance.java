package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.EnduranceThisAndNextTurnPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class Endurance extends BaseCard {
    public static final String ID = "basicmod:Endurance";
    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1  // 1 费
    );

    public Endurance() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;

        // 本卡为消耗，升级后取消消耗
        this.exhaust = true;

        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 下回合开始生效
        addToBot(new ApplyPowerAction(p, p, new EnduranceThisAndNextTurnPower(p), 1));
    }
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 移除消耗
            this.exhaust = false;

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Endurance();
    }
}
