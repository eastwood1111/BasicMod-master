package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LeftBowB extends BaseCard {

    public static final String ID = makeID(LeftBowB.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ENEMY,
            1 // 消耗 1
    );

    private static final int DAMAGE = 1;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int HIT_COUNT = 8;

    public LeftBowB() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();

        // ⚡ 初始化 baseDamage，让 !D! 显示正确
        this.baseDamage = DAMAGE;
        this.damage = this.baseDamage;

        this.exhaust = true; // 消耗
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int damagePerHit = upgraded ? UPGRADE_DAMAGE : DAMAGE;

        for (int i = 0; i < HIT_COUNT; i++) {
            // 临时修改 baseDamage 计算增益/减益
            this.baseDamage = damagePerHit;
            this.calculateCardDamage(m);

            addToBot(new DamageAction(
                    m,
                    new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.SLASH_DIAGONAL
            ));
        }

        // 恢复 baseDamage，保证 !D! 显示正确
        this.baseDamage = DAMAGE;
        if (upgraded) this.baseDamage = UPGRADE_DAMAGE;
        this.damage = this.baseDamage;
    }
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.baseDamage = UPGRADE_DAMAGE;
            this.damage = this.baseDamage;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LeftBowB();
    }
}
