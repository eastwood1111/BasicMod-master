package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.DrawNextTurnPower;  // 引入下回合抽牌 Power
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

public class SpiritSight extends BaseCard {  // 卡牌名称为灵视
    public static final String ID = "basicmod:SpiritSight";  // 卡牌ID
    private static final String IMG = "basicmod/images/cards/skill/default.png";  // 图片路径

    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1  // 1费
    );

    public SpiritSight() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();

        // 初始化其他属性
        this.exhaust = false;  // 不消耗
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用卡牌时，给玩家增加下回合抽牌效果的 Power
        int drawAmount = 2;  // 初始为2张

        // 升级后，抽3张牌
        if (this.upgraded) {
            drawAmount = 3;
        }

        // 检查玩家是否已经有 DrawNextTurnPower，如果有，叠加数值
        if (p.hasPower(DrawNextTurnPower.POWER_ID)) {
            DrawNextTurnPower existingPower = (DrawNextTurnPower) p.getPower(DrawNextTurnPower.POWER_ID);
            existingPower.amount += drawAmount;  // 叠加效果
        } else {
            // 如果没有该 Power，赋予新的 DrawNextTurnPower
            addToBot(new ApplyPowerAction(p, p, new DrawNextTurnPower(p, drawAmount), 1));  // 赋予下回合抽牌 Power
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级后，增加抽牌数量
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpiritSight();
    }
}
