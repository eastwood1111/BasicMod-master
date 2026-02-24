package basicmod.cards.skill;

import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.util.CardStats;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;

public class EscapeB extends BaseCard {

    public static final String ID = makeID(EscapeB.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            1 // 初始消耗
    );

    private static final int HP_LOSS = 6;      // 未升级失去生命
    private static final int UPG_HP_LOSS = 4;  // 升级版失去生命

    public EscapeB() {
        super(ID, info);
        this.baseMagicNumber = this.magicNumber = HP_LOSS;
        this.name = cardStrings.NAME;
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Boss房间无法逃脱
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            AbstractDungeon.effectList.add(new ThoughtBubble(
                    p.dialogX, p.dialogY, 3.0f,
                    "Boss房间无法逃脱！", true
            ));
            return;
        }

        // 玩家失去生命
        addToBot(new LoseHPAction(p, p, this.magicNumber));

        // 播放烟雾弹效果
        AbstractDungeon.effectList.add(new SmokeBombEffect(p.drawX, p.drawY));

        // 标记房间完成
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.magicNumber = this.baseMagicNumber = UPG_HP_LOSS;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new EscapeB();
    }
}
