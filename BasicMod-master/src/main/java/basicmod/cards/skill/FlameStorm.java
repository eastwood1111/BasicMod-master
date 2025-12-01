package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.CurrentStancePower;
import basicmod.powers.FlameStormPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class FlameStorm extends BaseCard {

    public static final String ID = makeID(FlameStorm.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2 // 消耗
    );

    private static final int DAMAGE_PER_HIT = 3;
    private static final int BASE_HITS = 3;
    private static final int UPG_HITS = 4;
    private static final int FIRE_STACK = 5; // 炎上层数

    public FlameStorm() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;

        this.baseDamage = DAMAGE_PER_HIT;
        this.damage = this.baseDamage; // 初始化显示
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean isMagic = false;

        if (p.hasPower(CurrentStancePower.POWER_ID)) {
            CurrentStancePower stancePower = (CurrentStancePower) p.getPower(CurrentStancePower.POWER_ID);
            isMagic = stancePower.getCurrentStance() == CurrentStancePower.Stance.MAGIC;
        }

        // 生成独立 Power，Power 内部会加智力
        addToBot(new ApplyPowerAction(
                p,
                p,
                new FlameStormPower(
                        p,
                        DAMAGE_PER_HIT,
                        upgraded ? UPG_HITS : BASE_HITS,
                        isMagic ? FIRE_STACK : 0
                )
        ));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlameStorm();
    }
}
