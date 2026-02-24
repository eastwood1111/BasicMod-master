package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.cards.skill.AdorationRing;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.BleedPower;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class ShieldStab extends BaseCard {

    public static final String ID = makeID(ShieldStab.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            1 // 消耗1费
    );

    private static final int DAMAGE = 5;
    private static final int BLOCK = 5;
    private static final int BLEED = 2;

    public ShieldStab() {
        super(ID, info);

        this.baseDamage = DAMAGE;
        this.baseBlock = BLOCK;
        this.baseMagicNumber = this.magicNumber = BLEED;

        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成伤害
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));

        // 获得格挡
        addToBot(new GainBlockAction(p, p, this.block));

        // 给予出血
        addToBot(new ApplyPowerAction(m, p, new BleedPower(m, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();

            // 升级后三者增加到7
            this.baseDamage = 7;
            this.baseBlock = 7;
            this.baseMagicNumber = this.magicNumber = 3;

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShieldStab();
    }
}
