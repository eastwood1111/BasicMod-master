package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class GunPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:GunPower";
    public static final String NAME = "猎人的火枪已经上膛";
    public static final String[] DESCRIPTIONS = {"每次打出攻击牌时，使敌方获得1层易伤。"};

    public GunPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        if (card.type != AbstractCard.CardType.ATTACK) return;

        AbstractPlayer p = (AbstractPlayer) this.owner;

        // 判断卡牌是否是攻击类
        if (card.target == AbstractCard.CardTarget.ENEMY) {
            // 单体攻击 -> 给目标敌人叠加
            AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
            if (m != null) {
                addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, 1, false), 1));
            }
        } else if (card.target == AbstractCard.CardTarget.ALL_ENEMY) {
            // 全体攻击 -> 遍历所有敌人
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, 1, false), 1));
                }
            }
        }
    }


    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new GunPower(owner);
    }
}
