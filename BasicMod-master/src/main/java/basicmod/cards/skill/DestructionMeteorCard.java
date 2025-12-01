package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.MeteorNextTurnPower;
import basicmod.powers.CurrentStancePower;
import basicmod.util.CardStats;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DestructionMeteorCard extends BaseCard {

    public static final String ID = makeID(DestructionMeteorCard.class.getSimpleName());
    private static final String IMG = "basicmod/images/cards/skill/default.png";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.NONE,
            1
    );

    private static final int DAMAGE = 2;
    private static final int UPG_DAMAGE = 3;
    private static final int HITS = 2;
    private static final int MAGIC_MODE_EXTRA = 3; // 魔法架势额外次数

    public DestructionMeteorCard() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();

        this.baseDamage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = HITS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        int hits = this.magicNumber;

        // 若处于魔法架势，次数 +3
        CurrentStancePower stance = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);
        if (stance != null && stance.getCurrentStance() == CurrentStancePower.Stance.MAGIC) {
            hits += MAGIC_MODE_EXTRA;
        }

        // 每张卡牌生成独立 Power
        addToBot(new ApplyPowerAction(
                p, p, new MeteorNextTurnPower(p, this.baseDamage, hits), 0
        ));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DestructionMeteorCard();
    }
}
