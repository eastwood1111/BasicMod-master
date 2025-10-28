package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.SoulFiveFormPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.BufferPower;

public class SoulFiveFormCard extends BaseCard {
    public static final String ID = makeID(SoulFiveFormCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            3 // 3费
    );

    public SoulFiveFormCard() {
        super(ID, info);
        this.isEthereal = true; // 虚无  this.rawDescription = DESCRIPTION; // 基础文本
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 基础一次性增益
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 1)));
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, 1)));
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1)));
        addToBot(new ApplyPowerAction(p, p, new BufferPower(p, 1)));

        // 添加持续 Power：每回合抽牌+1，每回合能量+1
        addToBot(new ApplyPowerAction(p, p, new SoulFiveFormPower(p, 1)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.isEthereal = false; // 升级后取消虚无
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SoulFiveFormCard();
    }
}
