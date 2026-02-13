package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.EnduranceThisAndNextTurnPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Endurance extends BaseCard {
    public static final String ID = makeID(Endurance.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    public Endurance() {
        super(ID, info);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 每次使用增加 1 层持续时间
        addToBot(new ApplyPowerAction(p, p, new EnduranceThisAndNextTurnPower(p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.exhaust = false;
            // 如果你的 JSON 里的描述在升级后有变化（比如去掉了“消耗”关键词），BaseCard 会处理。
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Endurance();
    }
}