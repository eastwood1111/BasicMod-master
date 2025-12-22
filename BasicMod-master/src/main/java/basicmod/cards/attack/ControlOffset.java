package basicmod.cards.attack; // 请修改为你的包名

import basicmod.cards.BaseCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import basicmod.charater.MyCharacter;// 请确认你的角色类路径
import basicmod.powers.CurrentStancePower; // 请确认你的Power类路径
import basicmod.util.CardStats; // 请确认你的CardStats类路径

public class ControlOffset extends BaseCard {  // 卡牌名称为控制抵消
    public static final String ID = "basicmod:ControlOffset";  // 卡牌ID，请确保前缀正确
    private static final String IMG = "basicmod/images/cards/attack/default.png"; // 建议替换为实际图片路径

    private static final CardStrings cardStrings =
            CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, // 你的角色颜色
            CardType.ATTACK,
            CardRarity.UNCOMMON, // 罕见是 UNCOMMON，如果你想要稀有(金卡)请改为 RARE
            CardTarget.ENEMY,
            1  // 1费
    );

    private static final int DAMAGE = 10;  // 初始伤害值
    private static final int UPGRADE_PLUS_DMG = 4;  // 升级增加的伤害值 (10+4=14)
    private static final int ARTIFACT_AMOUNT = 1; // 人工制品层数

    public ControlOffset() {
        super(ID, info);

        // 如果你的BaseCard没有自动处理本地化，保留这两行
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;

        this.baseDamage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = ARTIFACT_AMOUNT;

        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 造成伤害
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));

        // 2. 判断架势逻辑
        // 先判断是否存在这个Power，防止报错
        if (p.hasPower(CurrentStancePower.POWER_ID)) {
            CurrentStancePower stancePower = (CurrentStancePower) p.getPower(CurrentStancePower.POWER_ID);

            // 判断是否处于 韧狗 (IRON) 架势
            // 假设你的 CurrentStancePower.Stance 枚举里有 IRON
            if (stancePower != null && stancePower.getCurrentStance() == CurrentStancePower.Stance.IRON) {
                // 获得人工制品
                addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, magicNumber), magicNumber));
            }
        }
    }

    // 可选：让卡牌在符合条件时发光
    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        AbstractPlayer p = AbstractDungeon.player;

        if (p.hasPower(CurrentStancePower.POWER_ID)) {
            CurrentStancePower stancePower = (CurrentStancePower) p.getPower(CurrentStancePower.POWER_ID);
            if (stancePower != null && stancePower.getCurrentStance() == CurrentStancePower.Stance.IRON) {
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG); // 升级增加4点伤害
            initializeDescription();
        }
    }
}