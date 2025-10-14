package basicmod.cards;

import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class Defend extends BaseCard {
    public static final String ID = makeID(Defend.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, // 角色卡牌颜色
            CardType.SKILL,              // 技能牌
            CardRarity.BASIC,            // 基础稀有度
            CardTarget.SELF,             // 作用对象：自己
            1                            // 能量消耗
    );

    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 3;

    public Defend() {
        super(ID, info);

        setBlock(BLOCK, UPG_BLOCK); // 设置格挡数及升级增加值

        tags.add(CardTags.STARTER_DEFEND); // 标记为初始格挡牌
        // tags.add(CardTags.DEFEND); // 如果需要也可加
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 给玩家自己增加格挡
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Defend();
    }
}

