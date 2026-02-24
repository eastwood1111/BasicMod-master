package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.CometAzrePower;
import basicmod.powers.CurrentStancePower;
import basicmod.powers.MagicPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.relics.ChemicalX;

public class CometAzre extends BaseCard {
    public static final String ID = makeID(CometAzre.class.getSimpleName());
    private static final String IMG = "images/cards/skill/default.png";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY,
            -1
    );

    // 基础伤害
    private static final int BASE_DAMAGE = 8;
    private static final int UPGRADE_BASE_DAMAGE = 11;

    // 魔法架势额外加成（用 magicNumber 展示 !M!）
    private static final int MAGIC_STANCE_BONUS = 4;
    private static final int UPGRADE_MAGIC_STANCE_BONUS = 5;

    public CometAzre() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();

        setDamage(BASE_DAMAGE, UPGRADE_BASE_DAMAGE);
        setMagic(MAGIC_STANCE_BONUS, UPGRADE_MAGIC_STANCE_BONUS);
    }

    // =========================
    // 关键：屏蔽力量等，自己算显示伤害
    // =========================
    @Override
    public void applyPowers() {
        int finalDmg = getFinalDamageForDisplay();
        this.damage = finalDmg;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int finalDmg = getFinalDamageForDisplay();
        this.damage = finalDmg;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    private int getFinalDamageForDisplay() {
        if (AbstractDungeon.player == null) return this.baseDamage;

        AbstractPlayer p = AbstractDungeon.player;
        int intel = getIntel(p);

        int finalDamage = this.baseDamage + intel;

        CurrentStancePower stancePower = (CurrentStancePower) p.getPower(CurrentStancePower.POWER_ID);
        if (stancePower != null && stancePower.getCurrentStance() == CurrentStancePower.Stance.MAGIC) {
            finalDamage += this.magicNumber; // 魔法架势额外加成
        }

        return finalDamage;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) effect = this.energyOnUse;

        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
        }
        if (effect <= 0) return;

        int base = this.baseDamage;        // 8/11
        int stanceBonus = this.magicNumber; // 4/5（如果你把架势加成放 magicNumber）

        int baseForPower = base; // ✅ 只算基础，不算智力

        CurrentStancePower stancePower = (CurrentStancePower) p.getPower(CurrentStancePower.POWER_ID);
        if (stancePower != null && stancePower.getCurrentStance() == CurrentStancePower.Stance.MAGIC) {
            baseForPower += stanceBonus;   // ✅ 只加架势，不加智力
        }

        String uniqueID = String.valueOf(System.nanoTime());
        addToBot(new ApplyPowerAction(
                p, p, new CometAzrePower(p, baseForPower, effect, uniqueID), baseForPower
        ));

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }


    private int getIntel(AbstractPlayer p) {
        if (p == null) return 0;
        MagicPower mp = (MagicPower) p.getPower(MagicPower.POWER_ID);
        return mp == null ? 0 : mp.amount;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_BASE_DAMAGE - BASE_DAMAGE); // 8 -> 11
            upgradeMagicNumber(UPGRADE_MAGIC_STANCE_BONUS - MAGIC_STANCE_BONUS); // 4 -> 5
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CometAzre();
    }
}
