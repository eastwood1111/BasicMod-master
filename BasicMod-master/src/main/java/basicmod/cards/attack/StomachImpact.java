package basicmod.cards.attack;

import basicmod.cards.BaseCard;
import basicmod.cards.skill.AdorationRing;
import basicmod.powers.StomachImpactPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import basicmod.charater.MyCharacter;// 请确认你的角色类路径
import basicmod.powers.CurrentStancePower; // 请确认你的Power类路径
import basicmod.util.CardStats;

public class StomachImpact extends BaseCard {  // 卡牌名称：胃袋冲击
    public static final String ID = makeID(StomachImpact.class.getSimpleName());
    private static final String IMG = "basicmod/images/cards/attack/default.png"; // 请替换图片

    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.ATTACK, // 虽然是延迟伤害，但性质上通常归为攻击
            CardRarity.UNCOMMON,
            CardTarget.ENEMY, // 初始目标是单体
            1
    );

    private static final int DAMAGE = 10;

    public StomachImpact() {
        super(ID, info);
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;

        this.baseDamage = DAMAGE;

        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean isAOE = this.upgraded;
        int dmg = this.damage;

        // 检查玩家是否已经有这个 Power
        if (p.hasPower(StomachImpactPower.POWER_ID)) {
            StomachImpactPower power = (StomachImpactPower) p.getPower(StomachImpactPower.POWER_ID);
            power.addTarget(m, isAOE); // 直接调用自定义方法添加目标
            power.flash(); // 闪烁一下提示叠加成功
        } else {
            // 如果没有，施加新的
            addToBot(new ApplyPowerAction(p, p,
                    new StomachImpactPower(p, m, dmg, isAOE), 1));
        }
    }
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级后目标变为全体
            this.target = CardTarget.ALL_ENEMY;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}