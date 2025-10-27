package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.SpearPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SpearCard extends BaseCard {
    public static final String ID = makeID(SpearCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            1 // 初始费用
    );

    public SpearCard() {
        super(ID, info);
        this.name = "矛";
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 给自己添加一个Power，显示存在
        SpearPower power = (SpearPower)p.getPower(SpearPower.POWER_ID);
        if (power == null) {
            addToBot(new ApplyPowerAction(p, p, new SpearPower(p)));
        }

        // 判断攻击目标，如果是全体攻击则移除全体敌人格挡，否则只移除目标怪物格挡
        if (this.target == CardTarget.ALL_ENEMY) {
            for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
                monster.loseBlock(); // 直接调用原版方法移除格挡
            }
        } else if (m != null) {
            m.loseBlock();
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(0); // 升级后减费
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpearCard();
    }
}
