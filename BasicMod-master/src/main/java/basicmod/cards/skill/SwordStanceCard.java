package basicmod.cards.skill;

import basicmod.Enums;
import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import basicmod.powers.CurrentStancePower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SwordStanceCard extends BaseCard {
    public static final String ID = makeID(SwordStanceCard.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            1 // 初始能量消耗为 1
    );

    // 修改基础数值为 2
    private static final int BASE_STR = 3;

    public SwordStanceCard() {
        super(ID, info);

        // 设置 MagicNumber 为 2
        this.baseMagicNumber = this.magicNumber = BASE_STR;

        this.selfRetain = true;
        this.tags.add(Enums.STANCE);

        // 初始化描述，确保 !M! 和关键词正确渲染
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用标准的 addToBot 触发切换逻辑
        addToBot(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
            @Override
            public void update() {
                CurrentStancePower stancePower = (CurrentStancePower)p.getPower(CurrentStancePower.POWER_ID);
                if (stancePower == null) {
                    stancePower = new CurrentStancePower(p);
                    p.addPower(stancePower);
                }
                // 使用当前的 magicNumber (即 2)
                stancePower.switchStance(CurrentStancePower.Stance.SWORD, magicNumber);
                this.isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();

            // 修改点：升级后费用变为 0
            upgradeBaseCost(0);

            // 注意：因为数值保持为 2 不变，所以不需要调用 upgradeMagicNumber

            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SwordStanceCard();
    }
}