package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.BleedPower;  // 引入出血 Power
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;

public class TwinShortSword extends BaseCard {  // 卡牌名称为双小曲剑
    public static final String ID = "basicmod:TwinShortSword";  // 卡牌ID
    private static final String IMG = "basicmod/images/cards/attack/default.png";  // 图片路径

    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1  // 1费
    );

    private static final int DAMAGE = 1;  // 初始伤害
    private static final int UPGRADE_DAMAGE = 2;  // 升级后伤害
    private static final int BLEED_AMOUNT = 2;  // 给予敌人 2 层出血

    public TwinShortSword() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.baseDamage = DAMAGE;

        // 【重要】开启多重伤害标记，这样游戏才会自动计算 multiDamage 数组
        this.isMultiDamage = true;

        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 计算伤害，并执行 4 次伤害动作
        this.calculateCardDamage(m);  // 计算伤害，确保伤害值正确
        for (int i = 0; i < 4; i++) {
            addToBot(new DamageAction(
                    m,
                    new com.megacrit.cardcrawl.cards.DamageInfo(p, this.multiDamage[0], this.damageTypeForTurn),
                    com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect.SLASH_DIAGONAL
            ));
        }

        // 给敌人施加出血状态
        addToBot(new ApplyPowerAction(m, p, new BleedPower(m, BLEED_AMOUNT), BLEED_AMOUNT));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级后，增加伤害
            upgradeDamage(UPGRADE_DAMAGE - DAMAGE);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TwinShortSword();
    }
}
