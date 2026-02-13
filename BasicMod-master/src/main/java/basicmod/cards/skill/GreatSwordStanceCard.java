package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.CurrentStancePower;
// 删除了 DrawCardAction 的引用
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;

public class GreatSwordStanceCard extends BaseCard {
    public static final String ID = makeID(GreatSwordStanceCard.class.getSimpleName());
    private static final CardStrings cardStrings = com.megacrit.cardcrawl.core.CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1
    );

    private static final int BASE_STR = 3;
    private static final int UPG_STR = 4;

    public GreatSwordStanceCard() {
        super(ID, info);
        this.name = NAME;
        this.baseMagicNumber = this.magicNumber = BASE_STR;

        // 建议：如果在 JSON 文件中将描述写为 "获得 !M! 点力量..."，
        // 游戏会自动处理数值替换，就不需要下面这行 replace 代码了。
        // 这里为了保持兼容性，保留你的写法。
        this.rawDescription = DESCRIPTION.replace("!STR!", String.valueOf(BASE_STR));

        this.tags.add(basicmod.Enums.STANCE);
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        CurrentStancePower stancePower = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);

        // 注意：直接在这里 new Power 并 addPower 是即时生效的，
        // 但标准的写法通常是用 addToBot(new ApplyPowerAction(...))。
        // 不过鉴于你需要立即调用 switchStance，这里保持原样。
        if (stancePower == null) {
            stancePower = new CurrentStancePower(p);
            p.addPower(stancePower);
        }

        stancePower.switchStance(CurrentStancePower.Stance.GREAT_SWORD, this.magicNumber);

        // 已删除：升级后的抽牌逻辑
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 升级力量：从 3 变成 4 (增加 1)
            upgradeMagicNumber(UPG_STR - BASE_STR);

            // 更新描述：只更新数值，不再添加抽牌文本
            this.rawDescription = DESCRIPTION.replace("!STR!", String.valueOf(UPG_STR));

            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GreatSwordStanceCard();
    }
}