package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ZombifyCard extends BaseCard {

    public static final String ID = "basicmod:ZombifyCard";
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,   // 颜色
            CardType.SKILL,                // 技能
            CardRarity.UNCOMMON,               // 罕见
            CardTarget.SELF,               // 目标自己
            1                              // 费用
    );

    public ZombifyCard() {
        super(ID, info);
        this.baseMagicNumber = this.magicNumber = 5; // 最大生命损失
        this.baseHeal = this.heal = 8;               // 回复生命
        this.exhaust = true;
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION.replace("NL","\n");
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 真实减少最大生命
        p.decreaseMaxHealth(this.magicNumber);

        // 保证当前生命不会超过新最大值
        if (p.currentHealth > p.maxHealth) {
            p.currentHealth = p.maxHealth;
        }

        // 回复生命
        addToBot(new HealAction(p, p, this.heal));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.magicNumber = 4; // 升级后减少最大生命 4
            this.heal = 10;        // 回复生命 5
            this.rawDescription = cardStrings.DESCRIPTION.replace("NL","\n");
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ZombifyCard();
    }
}
