package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.MarathonBDelayedEntanglePower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class MarathonB extends BaseCard {

    public static final String ID = makeID(MarathonB.class.getSimpleName());
    private static final CardStats stats = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1 // 1费
    );

    private static final int BLOCK = 20;
    private static final int UPGRADE_BLOCK = 25;

    public MarathonB() {
        super(ID, stats);

        this.baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得格挡
        addToBot(new GainBlockAction(p, p, this.block));

        // 下回合开始赋予缠身
        addToBot(new ApplyPowerAction(p, p, new MarathonBDelayedEntanglePower(p), 1));
    }


    @Override
    public AbstractCard makeCopy() {
        return new MarathonB();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK - BLOCK); // 20 → 25
        }
    }
}
