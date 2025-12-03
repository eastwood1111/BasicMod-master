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

public class LiSkillCard extends BaseCard {

    public static final String ID = makeID(LiSkillCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK,
            CardRarity.RARE,
            CardTarget.ALL,
            2 // 初始费用
    );

    private static final int DAMAGE = 2;
    private static final int HIT_COUNT = 8;
    private static final int UPG_HIT_COUNT = 10;

    public LiSkillCard() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        this.initializeDescription();

        this.baseDamage = DAMAGE;
        this.damage = this.baseDamage;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;

        // 获取上一张打出的牌
        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.isEmpty()) {
            this.cantUseMessage = "上一张牌不是攻击牌，无法打出！";
            return false;
        }

        AbstractCard lastCard = AbstractDungeon.actionManager.cardsPlayedThisCombat
                .get(AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1);

        if (lastCard.type != CardType.ATTACK) {
            this.cantUseMessage = "上一张牌不是攻击牌，无法打出！";
            return false;
        }

        return true;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int hitCount = upgraded ? UPG_HIT_COUNT : HIT_COUNT;
        Random rand = new Random();

        List<AbstractMonster> monsters = new ArrayList<>(AbstractDungeon.getCurrRoom().monsters.monsters);

        // 临时保存初始的伤害值
        int originalDamage = this.baseDamage;

        for (int i = 0; i < hitCount; i++) {
            if (monsters.isEmpty()) break;

            AbstractMonster target = monsters.get(rand.nextInt(monsters.size()));

            // 计算伤害，基于当前目标更新伤害值
            this.baseDamage = DAMAGE;  // 你可以根据实际需求动态更新 baseDamage
            this.calculateCardDamage(target); // 更新当前卡牌的伤害值

            // 确保每次攻击后伤害正确
            addToBot(new DamageAction(
                    target,
                    new DamageInfo(p, this.damage, DamageInfo.DamageType.NORMAL),
                    AbstractGameAction.AttackEffect.SLASH_HEAVY
            ));
        }

        // 恢复原始的 baseDamage 和 damage
        this.baseDamage = originalDamage;
        this.damage = this.baseDamage;
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
        return new LiSkillCard();
    }
}
