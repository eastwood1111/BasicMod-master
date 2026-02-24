package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.MagicPower;
import basicmod.powers.SunGunBuffPower;
import basicmod.powers.CurrentStancePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SunGun extends BaseCard {
    public static final String ID = makeID(SunGun.class.getSimpleName());
    private static final String IMG = "images/cards/skill/default.png";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.NONE,
            1
    );

    private static final int DAMAGE = 14;
    private static final int UPG_DAMAGE = 16;

    public SunGun() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();

        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 结算仍然只用基础值（你原本就是这样）
        int baseDamage = this.baseDamage;

        String uniqueID = String.valueOf(System.nanoTime());
        SunGunBuffPower power = new SunGunBuffPower(p, baseDamage, uniqueID);
        addToBot(new ApplyPowerAction(p, p, power, baseDamage));

        CurrentStancePower stancePower = (CurrentStancePower) p.getPower(CurrentStancePower.POWER_ID);
        if (stancePower != null && stancePower.getCurrentStance() == CurrentStancePower.Stance.MAGIC) {
            String uniqueID2 = String.valueOf(System.nanoTime() + 1);
            SunGunBuffPower extraPower = new SunGunBuffPower(p, baseDamage, uniqueID2);
            addToBot(new ApplyPowerAction(p, p, extraPower, baseDamage));
        }
    }

    // =========================
    // 关键：屏蔽力量，自己加智力到“显示”
    // =========================
    @Override
    public void applyPowers() {
        // 不调用 super.applyPowers(); 避免 Strength 影响显示
        int intelBonus = getIntBonus();
        this.damage = this.baseDamage + intelBonus;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        // 同理，不调用 super.calculateCardDamage();
        int intelBonus = getIntBonus();
        this.damage = this.baseDamage + intelBonus;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    private int getIntBonus() {
        if (AbstractDungeon.player == null) return 0;

        MagicPower mp = (MagicPower) AbstractDungeon.player.getPower(MagicPower.POWER_ID);
        if (mp == null) return 0;

        // ✅ 这里按你的智力公式改：
        return mp.amount; // 例：智力=amount，显示时加到数值上
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE - DAMAGE);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SunGun();
    }
}
