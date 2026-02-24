package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.cards.skill.AdorationRing;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class NotNot extends BaseCard {

    public static final String ID = makeID(NotNot.class.getSimpleName());
    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            3   // 费用
    );

    private static final int DMG_OR_BLOCK = 12;
    private static final int UPG_VALUE = 15;
    private static final int TIMES = 3; // 固定三次

    public NotNot() {
        super(ID, info);
        this.name = cardStrings.NAME;

        this.baseDamage = DMG_OR_BLOCK;
        this.baseBlock = DMG_OR_BLOCK;

        this.magicNumber = this.baseMagicNumber = TIMES;

        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        for (int i = 0; i < this.magicNumber; i++) {
            // 50% 伤害 / 50% 格挡
            if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                // 造成伤害
                addToBot(new DamageAction(
                        m,
                        new com.megacrit.cardcrawl.cards.DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
                ));
            } else {
                // 获得格挡
                addToBot(new GainBlockAction(p, p, this.block));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();

            upgradeDamage(UPG_VALUE - DMG_OR_BLOCK);
            upgradeBlock(UPG_VALUE - DMG_OR_BLOCK);

            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NotNot();
    }
}
