package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.UnlockedCrossbowPower; // 自定义Power

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class UnlockedCrossbow extends BaseCard {

    public static final String ID = "basicmod:UnlockedCrossbow";
    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0
    );

    private static final int BURN = 2;

    public UnlockedCrossbow() {
        super(ID, info);
        this.baseMagicNumber = this.magicNumber = BURN;

        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 给自己添加一个Power，用于监听打出的攻击牌
        addToBot(new ApplyPowerAction(
                p, p, new UnlockedCrossbowPower(p, this.magicNumber), this.magicNumber
        ));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1); // 数值从 2 变 3

            // 核心：必须重新读取描述，底层逻辑才会重新扫描 !M! 占位符
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;

            initializeDescription(); // 刷新显示
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UnlockedCrossbow();
    }
}
