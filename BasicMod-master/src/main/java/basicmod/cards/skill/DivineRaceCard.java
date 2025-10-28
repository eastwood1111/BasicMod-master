package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.DivineRacePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

public class DivineRaceCard extends BaseCard {
    public static final String ID = makeID(DivineRaceCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );

    public DivineRaceCard() {
        super(ID, info);
        this.selfRetain = false; // 默认不保留
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 当前手牌费用全部减为0（本回合）
        for (AbstractCard c : p.hand.group) {
            if (c.cost >= 0) { // 排除 X 费和不可用卡
                c.setCostForTurn(0);
            }
        }

        // 给自己添加 DivineRacePower，初始层数 1
        addToBot(new ApplyPowerAction(p, p, new DivineRacePower(p, 1)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.selfRetain = true; // 升级后保留
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DivineRaceCard();
    }
}
