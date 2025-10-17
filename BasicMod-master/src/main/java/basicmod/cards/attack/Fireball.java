package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class Fireball extends BaseCard {
    public static final String ID = makeID(Fireball.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, // 蓝色角色颜色
            CardType.ATTACK,             // 攻击牌
            CardRarity.UNCOMMON,         // 蓝色稀有度
            CardTarget.ALL_ENEMY,        // 攻击全体敌人
            2                            // 消耗2能量
    );

    private static final int DAMAGE = 16;
    private static final int UPG_DAMAGE = 4; // 升级后总共20（16+4）

    public Fireball() {
        super(ID, info);

        setDamage(DAMAGE, UPG_DAMAGE);
        this.isMultiDamage = true; // 表示攻击全体
        this.exhaust = true;       // 使用后消耗
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(
                p,
                this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.FIRE
        ));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE);
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Fireball();
    }
}

