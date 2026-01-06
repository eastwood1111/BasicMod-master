package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.CurrentStancePower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class SwordStanceCard extends BaseCard {
    public static final String ID = makeID(SwordStanceCard.class.getSimpleName());
    // 已经在 BaseCard 里处理了图像路径，这里保留引用
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            1
    );

    private static final int BASE_STR = 1;
    private static final int UPG_STR = 1; // 升级增加的数值，这里根据你的逻辑调整

    public SwordStanceCard() {
        // 1. 先调用父类构造函数
        super(ID, info);

        // 2. 设置 MagicNumber。注意：必须在 initializeDescription 之前
        this.baseMagicNumber = this.magicNumber = BASE_STR;

        // 3. 【最关键一步】重新强制覆盖 rawDescription
        // 很多时候父类 super() 会过早锁定描述。我们在这里强行把带 #y 的字符串塞回去
        if (cardStrings != null) {
            this.rawDescription = cardStrings.DESCRIPTION;
        }

        // 4. 特殊属性设置
        this.selfRetain = true;
        this.tags.add(basicmod.Enums.STANCE);

        // 5. 执行解析。这一步会将 #y力量 转换为金黄色，并将 [ 剑 ] 识别为关键词
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用 addToBot 是正确的做法
        addToBot(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
            @Override
            public void update() {
                CurrentStancePower stancePower = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);
                if (stancePower == null) {
                    stancePower = new CurrentStancePower(p);
                    p.addPower(stancePower);
                }
                // 确保在 Action 中引用的是 magicNumber (受升级和力量影响的实时数值)
                stancePower.switchStance(CurrentStancePower.Stance.SWORD, magicNumber);
                this.isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            // 使用 BaseCard 提供的工具方法（如果可用）或直接修改
            upgradeMagicNumber(UPG_STR);

            // 升级时也要强制重刷新描述，否则 #y 依然可能失效
            if (cardStrings.UPGRADE_DESCRIPTION != null) {
                this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            } else {
                this.rawDescription = cardStrings.DESCRIPTION;
            }

            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SwordStanceCard();
    }
}