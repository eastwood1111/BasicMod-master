package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class ManaDrain extends BaseCard {

    public static final String ID = makeID(ManaDrain.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            0
    );

    public ManaDrain() {
        super(ID, info);

        this.exhaust = true; // 消耗
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int livingEnemies = (int) AbstractDungeon.getCurrRoom().monsters.monsters.stream()
                .filter(mon -> !mon.isDeadOrEscaped())
                .count();

        if (livingEnemies > 0) {
            addToBot(new GainEnergyAction(livingEnemies));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.isInnate = true; // 固有
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ManaDrain();
    }
}
