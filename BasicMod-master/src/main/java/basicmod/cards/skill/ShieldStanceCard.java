package basicmod.cards.skill;

import basicmod.Enums;
import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.CurrentStancePower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ShieldStanceCard extends BaseCard {
    public static final String ID = makeID(ShieldStanceCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            1 // 初始费用为 1
    );

    // 设定获得的值为 2
    private static final int BASE_DEX = 3;

    public ShieldStanceCard() {
        super(ID, info);

        // 设置 magicNumber 为 2
        this.baseMagicNumber = this.magicNumber = BASE_DEX;

        this.selfRetain = true;
        this.tags.add(Enums.STANCE);

        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CurrentStancePower stancePower = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);
        if (stancePower == null) {
            stancePower = new CurrentStancePower(p);
            p.addPower(stancePower);
        }

        // 切换姿态，数值传入 2 (magicNumber)
        stancePower.switchStance(CurrentStancePower.Stance.SHIELD, this.magicNumber);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();

            // 1. 升级后费用变为 0
            upgradeBaseCost(0);

            // 2. 如果不需要升级数值，则不需要调用 upgradeMagicNumber
            // 如果你想升级数值的同时降费，可以保留 upgradeMagicNumber

            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShieldStanceCard();
    }
}