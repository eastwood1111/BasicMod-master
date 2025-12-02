package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.CometAzrePower;
import basicmod.powers.CurrentStancePower;
import basicmod.powers.MagicPower; // 引入MagicPower来获取智力加成
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class CometAzre extends BaseCard {
    public static final String ID = makeID(CometAzre.class.getSimpleName());
    private static final String IMG = "images/cards/skill/default.png";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.ALL_ENEMY,
            -1  // X费用
    );

    // 基础伤害为 8
    private static final int BASE_DAMAGE = 8;
    // 魔法架势下增加 4 点伤害
    private static final int MAGIC_STANCE_DAMAGE = 4;

    // 升级后基础伤害为 11
    private static final int UPGRADE_BASE_DAMAGE = 11;
    // 升级后魔法架势下增加 5 点伤害
    private static final int UPGRADE_MAGIC_STANCE_DAMAGE = 5;

    public CometAzre() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();

        setDamage(BASE_DAMAGE, UPGRADE_BASE_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int energySpent = this.energyOnUse;  // 获取卡牌使用时消耗的能量
        int finalDamage = BASE_DAMAGE;  // 初始伤害为基础伤害

        // 判断是否处于魔法架势
        CurrentStancePower stancePower = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);
        if (stancePower != null && stancePower.getCurrentStance() == CurrentStancePower.Stance.MAGIC) {
            finalDamage = BASE_DAMAGE + MAGIC_STANCE_DAMAGE;  // 魔法架势下的伤害
        }

        // 创建并应用 Power，表示下回合造成的伤害
        addToBot(new ApplyPowerAction(p, p, new CometAzrePower(p, finalDamage, energySpent), finalDamage));

        // 消耗所有的能量
        if (!this.freeToPlayOnce) {
            p.energy.use(energySpent);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_BASE_DAMAGE - BASE_DAMAGE);  // 升级后增加基础伤害
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new CometAzre();
    }
}
