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

public class FireKingFiveCombo extends BaseCard {

    public static final String ID = makeID(FireKingFiveCombo.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ALL,
            2 // 初始费用
    );

    private static final int DAMAGE = 3;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int HIT_COUNT = 5;

    public FireKingFiveCombo() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int damagePerHit = upgraded ? UPGRADE_DAMAGE : DAMAGE;
        Random rand = new Random();

        List<AbstractMonster> monsters = new ArrayList<>(AbstractDungeon.getCurrRoom().monsters.monsters);

        for (int i = 0; i < HIT_COUNT; i++) {
            if (monsters.isEmpty()) break;

            AbstractMonster target = monsters.get(rand.nextInt(monsters.size()));

            // 设置临时伤害值
            this.baseDamage = damagePerHit;
            this.calculateCardDamage(target); // 计算增益/减益影响后的 damage

            addToBot(new DamageAction(
                    target,
                    new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
            ));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FireKingFiveCombo();
    }
}
