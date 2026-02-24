package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.cards.skill.AdorationRing;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.CurseDeathPower;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class CurseDagger extends BaseCard {

    public static final String ID = makeID(CurseDagger.class.getSimpleName());
    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            0
    );

    private static final int DAMAGE = 3;
    private static final int CURSE = 1;

    public CurseDagger() {
        super(ID, info);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = CURSE;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        // 造成伤害
        addToBot(new DamageAction(
                m,
                new DamageInfo(p, this.damage, this.damageTypeForTurn)
        ));

        // 施加咒死
        addToBot(new ApplyPowerAction(
                m, p, new CurseDeathPower(m, this.magicNumber), this.magicNumber
        ));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            upgradeMagicNumber(1); // 1 → 2 层咒死
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CurseDagger();
    }
}
