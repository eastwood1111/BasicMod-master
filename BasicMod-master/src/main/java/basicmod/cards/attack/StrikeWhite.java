package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class StrikeWhite extends BaseCard {
    public static final String ID = makeID(StrikeWhite.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, // 白色卡池
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1 // 费用
    );

    private static final int DAMAGE = 10;

    public StrikeWhite() {
        super(ID, info);
        this.baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn))
        );
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(4); // 升级后伤害 +4（可自定义）
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new StrikeWhite();
    }
}
