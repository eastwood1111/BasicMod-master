package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.cards.skill.AdorationRing;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.FlamePower; // 自定义炎上Power

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FirePot extends BaseCard {
    public static final String ID = makeID(FirePot.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ALL_ENEMY,
            1 // 费用
    );

    private static final int DAMAGE = 5;
    private static final int UPG_DAMAGE = 3; // 升级后+3，原本5 -> 8
    private static final int BURN = 3; // 炎上层数

    public FirePot() {
        super(ID, info);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = BURN; // 用于炎上层数
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对所有敌人造成伤害
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            addToBot(new DamageAction(monster,
                    new DamageInfo(p, this.damage, this.damageTypeForTurn)));
            addToBot(new ApplyPowerAction(monster, p, new FlamePower(monster, p, this.magicNumber), this.magicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE); // 升级后伤害 5 -> 8
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FirePot();
    }
}
