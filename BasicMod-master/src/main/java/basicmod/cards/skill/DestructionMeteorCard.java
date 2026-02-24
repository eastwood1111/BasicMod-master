package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.MeteorNextTurnPower;
import basicmod.powers.CurrentStancePower;
import basicmod.powers.MagicPower;
import basicmod.util.CardStats;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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

    private static final int HITS = 4;
    private static final int MAGIC_MODE_EXTRA = 2; // 魔法架势额外次数

    public DestructionMeteorCard() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();

        setDamage(DAMAGE, UPG_DAMAGE);     // !D! 每次伤害
        setMagic(HITS);                    // !M! 基础次数
        setBlock(MAGIC_MODE_EXTRA);        // !B! 魔法架势额外次数（借用 block 展示）
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
        int hits = this.magicNumber;

        CurrentStancePower stance = (CurrentStancePower) p.getPower(CurrentStancePower.POWER_ID);
        if (stance != null && stance.getCurrentStance() == CurrentStancePower.Stance.MAGIC) {
            hits += this.block; // +3（升级不变）
        }

        int perHitDamage = this.baseDamage;

        String uniqueID = String.valueOf(System.nanoTime());
        addToBot(new ApplyPowerAction(
                p, p, new MeteorNextTurnPower(p, perHitDamage, hits, uniqueID), 0
        ));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE); // 2 -> 3
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DestructionMeteorCard();
    }
}
