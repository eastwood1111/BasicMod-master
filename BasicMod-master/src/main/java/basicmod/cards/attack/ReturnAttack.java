package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class ReturnAttack extends BaseCard {

    public static final String ID = "basicmod:ReturnAttack";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, // 蓝色角色
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            2 // 初始费用 2
    );

    public ReturnAttack() {
        super(ID, info);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            int intentDamage = m.getIntentBaseDmg();
            if (intentDamage < 0) intentDamage = 0;

            // 构造一个 DamageInfo，让系统自动计算力量/易伤加成
            DamageInfo damageInfo = new DamageInfo(p, intentDamage, DamageInfo.DamageType.NORMAL);

            // 让怪物被伤害时受力量/易伤加成
            damageInfo.applyPowers(p, m);

            addToBot(new DamageAction(
                    m,
                    damageInfo
            ));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.upgradeBaseCost(1); // 升级后费用 1
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ReturnAttack();
    }
}
