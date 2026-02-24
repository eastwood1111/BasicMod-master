package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.BleedPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame; // 必须导入
import com.megacrit.cardcrawl.localization.CardStrings; // 必须导入
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TwinShortSword extends BaseCard {
    public static final String ID = "basicmod:TwinShortSword";

    // --- 补充：必须声明 cardStrings 否则 upgrade() 会报错 ---
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1
    );

    public TwinShortSword() {
        super(ID, info);
        this.baseDamage = 1;
        this.baseMagicNumber = this.magicNumber = 2; // 出血层数
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 在 Action 执行前，this.damage 已经被引擎自动计算好了（包含力量、易伤、敏捷等）
        for (int i = 0; i < 4; i++) {
            addToBot(new DamageAction(m,
                    new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }

        // 应用出血
        addToBot(new ApplyPowerAction(m, p, new BleedPower(m, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 建议：升级后基础伤害加 1，变成 2 点。
            // 这样易伤就能体现出效果了 (2 * 1.5 = 3)
            upgradeDamage(1);

            // 确保你的 .json 文件里写了 UPGRADE_DESCRIPTION
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}