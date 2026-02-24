package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.cards.skill.AdorationRing;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Road365 extends BaseCard {
    public static final String ID = makeID(Road365.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.COMMON,
            CardTarget.ENEMY,
            1 // 初始费用
    );

    private static final int DAMAGE = 3;     // 基础伤害
    private static final int UPG_DAMAGE = 0; // 升级增加

    public Road365() {
        super(ID, info);
        this.baseDamage = DAMAGE;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 实际伤害在手牌显示已经计算，这里直接使用 damage
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
    }

    @Override
    public void applyPowers() {
        // 根据是否升级，调整遗物加成
        int relicBonus = (upgraded ? AbstractDungeon.player.relics.size() * 2 : AbstractDungeon.player.relics.size()); // 升级后每个遗物增加2点，否则是1点
        this.baseDamage = DAMAGE + relicBonus; // 临时调整基础伤害
        super.applyPowers(); // 计算力量、易伤等加成
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        // 根据是否升级，调整遗物加成
        int relicBonus = (upgraded ? AbstractDungeon.player.relics.size() * 2 : AbstractDungeon.player.relics.size()); // 升级后每个遗物增加2点，否则是1点
        this.baseDamage = DAMAGE + relicBonus; // 临时调整基础伤害
        super.calculateCardDamage(m); // 计算目标相关加成
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
        return new Road365();
    }
}
