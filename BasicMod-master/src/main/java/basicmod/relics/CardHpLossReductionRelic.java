package basicmod.relics;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import basicmod.charater.MyCharacter;

import static basicmod.BasicMod.makeID;

public class CardHpLossReductionRelic extends BaseRelic {
    // 遗物ID，需确保在你的 BaseMod 中正确注册了前缀
    public static final String ID = makeID("CardHpLossReductionRelic");

    public CardHpLossReductionRelic() {
        // 参数：ID, 贴图名, 角色颜色, 稀有度, 落地声音
        super(ID, "myrelic", MyCharacter.Meta.CARD_COLOR, RelicTier.RARE, LandingSound.HEAVY);
    }

    /**
     * 当玩家受到攻击/伤害时触发
     * @param info 伤害信息，包含来源和类型
     * @param damageAmount 原始伤害数值
     * @return 最终受到的伤害数值
     */
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        // 判断条件：
        // 1. damageAmount > 0 : 确实有伤害
        // 2. info.type != DamageInfo.DamageType.NORMAL :
        //    NORMAL 是敌人的普通攻击（会被格挡减免的）。
        //    排除掉 NORMAL 后，剩下的就是 HP_LOSS（失去生命，如卡牌代价、中毒）和 THORNS（荆棘、反伤）。
        if (damageAmount > 0 && info.type != DamageInfo.DamageType.NORMAL) {
            this.flash();

            // 减少 1 点，并确保不会降至 0 以下
            int finalAmount = damageAmount - 1;
            return Math.max(0, finalAmount);
        }

        // 如果是敌人的普通攻击，原样返回伤害，不触发减免
        return damageAmount;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CardHpLossReductionRelic();
    }
}