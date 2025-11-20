package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class TeachSong extends BaseCard {

    public static final String ID = "basicmod:TeachSong";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, // 蓝色角色
            CardType.ATTACK,
            CardRarity.UNCOMMON,
            CardTarget.ENEMY,
            -1 // X费用用 -1 表示
    );

    private static final int DAMAGE = 4;
    private static final int EXTRA_HITS = 1; // 基础 X+1 次

    public TeachSong() {
        super(ID, info);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = EXTRA_HITS; // 用来存储额外次数
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int times = this.magicNumber + this.energyOnUse; // X + 1 或升级后 X + 2

        for (int i = 0; i < times; i++) {
            addToBot(new DamageAction(
                    m,
                    new DamageInfo(p, this.damage, this.damageTypeForTurn),
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
            ));
        }

        // 如果是 X 费用，使用后消耗所有能量
        if (!this.freeToPlayOnce) {
            p.energy.use(this.energyOnUse);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(1); // 升级后额外次数 +1，即 X+2 次
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TeachSong();
    }
}
