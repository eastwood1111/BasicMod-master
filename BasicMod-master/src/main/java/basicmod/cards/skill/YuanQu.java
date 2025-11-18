package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.powers.LoseHpNextTurnPower;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class YuanQu extends BaseCard {
    public static final String ID = makeID(YuanQu.class.getSimpleName());
    private static final String IMG = "images/cards/skill/default.png";

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int HEAL = 4;
    private static final int UPG_HEAL = 6;

    public YuanQu() {
        super(ID, info);

        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();

        setMagic(HEAL, UPG_HEAL); // magicNumber 作为回复与下回合失血数量
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = this.magicNumber;

        // 立即回复生命
        addToBot(new HealAction(p, p, amount));

        // 下回合失去生命（使用 Power 记录）
        addToBot(new ApplyPowerAction(
                p, p, new LoseHpNextTurnPower(p, amount), amount
        ));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_HEAL - HEAL);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new YuanQu();
    }
}
