package basicmod.cards.skill;

import basicmod.Enums;
import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.CurrentStancePower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ShieldStanceCard extends BaseCard {
    public static final String ID = makeID(ShieldStanceCard.class.getSimpleName());

    // 1. 移除这里的 static cardStrings，防止加载顺序导致的空指针或读取不到文本
    // private static final CardStrings cardStrings = ... (删除)

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            1
    );

    private static final int BASE_DEX = 1;
    private static final int UPG_DEX = 2;

    public ShieldStanceCard() {
        super(ID, info);

        // 2. 设置数值
        // 将 baseMagicNumber 设置为你的 DEX 值
        this.baseMagicNumber = this.magicNumber = BASE_DEX;

        // 3. 处理描述
        // BaseCard (通常的模版) 会自动读取 ID 对应的 CardStrings 并赋值给 cardStrings
        // 如果你的 BaseCard 没有自动处理，可以使用下面的手动获取方式：
        // CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        // this.rawDescription = cardStrings.DESCRIPTION;

        // 注意：不要在这里用 replace("!DEX!", ...)。
        // 请在 JSON 中直接写 "获得 !M! 点格挡" (或者你的效果)，游戏会自动把 !M! 替换为 magicNumber 的值。

        this.selfRetain = true;
        this.tags.add(Enums.STANCE);

        // 初始化描述，这会将 JSON 中的 !M! 渲染为蓝色的数字
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CurrentStancePower stancePower = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);
        if (stancePower == null) {
            stancePower = new CurrentStancePower(p);
            p.addPower(stancePower);
        }

        // 使用 magicNumber，这样升级后数值会自动变化
        stancePower.switchStance(CurrentStancePower.Stance.SHIELD, this.magicNumber);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级 magicNumber
            upgradeMagicNumber(UPG_DEX - BASE_DEX);

            // 4. 不需要手动重置 rawDescription
            // 只要 JSON 里有 UPGRADE_DESCRIPTION，BaseCard 通常会自动处理。
            // 如果没有 UPGRADE_DESCRIPTION，它会保持原样，但 !M! 的数值会变。

            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShieldStanceCard();
    }
}