package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.GreenHerbUsedPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class GreenHerb extends BaseCard {
    public static final String ID = "basicmod:GreenHerb";
    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0
    );

    private static final int BASE_ENERGY = 1;
    private static final int UPG_ENERGY = 2;

    public GreenHerb() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.baseMagicNumber = this.magicNumber = BASE_ENERGY;

        initializeDescription();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        // 如果已有 GreenHerbUsedPower，禁止使用
        if (p.hasPower(GreenHerbUsedPower.POWER_ID)) {
            this.cantUseMessage = "本回合已使用过绿花草！";
            return false;
        }
        return super.canUse(p, m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得能量
        addToBot(new GainEnergyAction(this.magicNumber));

        // 给自己添加限制 Power
        addToBot(new ApplyPowerAction(p, p, new GreenHerbUsedPower(p), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_ENERGY - BASE_ENERGY); // 能量升级
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GreenHerb();
    }
}
