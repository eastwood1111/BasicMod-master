package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.StanceDefendPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

public class StanceDefendCard extends BaseCard {
    public static final String ID = makeID(StanceDefendCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    // 修改数值：基础 4，升级增加 2 (总计 6)
    private static final int BASE_STACK = 4;
    private static final int UPG_STACK = 2;

    public StanceDefendCard() {
        super(ID, info);

        // 设置 MagicNumber
        this.baseMagicNumber = this.magicNumber = BASE_STACK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 修正：ApplyPowerAction 的最后一个参数通常是该 Power 的叠加层数
        // 这样可以确保如果以后有其他效果增加层数，逻辑依然正确
        addToBot(new ApplyPowerAction(p, p, new StanceDefendPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级：从 4 增加 2 达到 6
            upgradeMagicNumber(UPG_STACK);

            // 如果你的 Power 描述在升级后需要改变，可以在这里设置
            // this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;

            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StanceDefendCard();
    }
}