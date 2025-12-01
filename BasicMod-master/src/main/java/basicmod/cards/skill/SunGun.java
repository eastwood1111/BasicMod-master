package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.MagicPower;
import basicmod.powers.SunGunBuffPower;
import basicmod.powers.CurrentStancePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;

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

    private static final int DAMAGE = 10;
    private static final int UPG_DAMAGE = 12;

    public SunGun() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();

        setDamage(DAMAGE, UPG_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int baseDamage = this.baseDamage; // 仅基础伤害，不加智力

        // 每张卡牌生成独立 Power
        String uniqueID = String.valueOf(System.nanoTime());
        SunGunBuffPower power = new SunGunBuffPower(p, baseDamage, uniqueID);
        addToBot(new ApplyPowerAction(p, p, power, baseDamage));

        // 魔法架势额外触发一次
        CurrentStancePower stancePower = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);
        if (stancePower != null && stancePower.getCurrentStance() == CurrentStancePower.Stance.MAGIC) {
            String uniqueID2 = String.valueOf(System.nanoTime() + 1);
            SunGunBuffPower extraPower = new SunGunBuffPower(p, baseDamage, uniqueID2);
            addToBot(new ApplyPowerAction(p, p, extraPower, baseDamage));
        }
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
