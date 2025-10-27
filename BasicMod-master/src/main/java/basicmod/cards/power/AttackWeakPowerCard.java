package basicmod.cards.power;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.AttackWeakPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AttackWeakPowerCard extends BaseCard {
    public static final String ID = makeID(AttackWeakPowerCard.class.getSimpleName());
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            1
    );

    private final boolean upgraded;

    public AttackWeakPowerCard() {
        super(ID, info);
        this.upgraded = false;
    }

    public AttackWeakPowerCard(boolean upgraded) {
        super(ID, info);
        this.upgraded = upgraded;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 给玩家添加一个 Power，记录触发逻辑
        if (!p.hasPower(AttackWeakPower.POWER_ID)) {
            p.addPower(new AttackWeakPower(p, upgraded));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new AttackWeakPowerCard(upgraded);
    }
}
