package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.CurrentStancePower;
import basicmod.powers.FlameStormPower;
import basicmod.powers.MagicPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
            2
    );

    private static final int DAMAGE_PER_HIT = 3;

    private static final int BASE_HITS = 3;
    private static final int UPG_HITS = 4;

    private static final int FIRE_STACK = 5;

    public FlameStorm() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();

        // 每段伤害
        setDamage(DAMAGE_PER_HIT);

        // 次数（描述里用 !M!）
        setMagic(BASE_HITS, UPG_HITS);

        // 魔法架势附加炎上层数（描述里用 !B!）
        setBlock(FIRE_STACK);
    }

    // =========================
    // 关键：屏蔽力量，自己加智力到“显示”
    // =========================
    @Override
    public void applyPowers() {
        int intelBonus = getIntBonus();
        this.damage = this.baseDamage + intelBonus;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int intelBonus = getIntBonus();
        this.damage = this.baseDamage + intelBonus;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    private int getIntBonus() {
        if (AbstractDungeon.player == null) return 0;
        MagicPower mp = (MagicPower) AbstractDungeon.player.getPower(MagicPower.POWER_ID);
        return mp == null ? 0 : mp.amount;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        boolean isMagic = false;
        CurrentStancePower stancePower =
                (CurrentStancePower) p.getPower(CurrentStancePower.POWER_ID);

        if (stancePower != null) {
            isMagic = stancePower.getCurrentStance()
                    == CurrentStancePower.Stance.MAGIC;
        }

        int perHitDamage = this.baseDamage;
        int hits = this.magicNumber;
        int fire = isMagic ? this.block : 0;

        String uniqueID = String.valueOf(System.nanoTime());

        addToBot(new ApplyPowerAction(
                p,
                p,
                new FlameStormPower(p, perHitDamage, hits, fire, uniqueID)
        ));
    }
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_HITS - BASE_HITS); // 3 -> 4
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlameStorm();
    }
}
