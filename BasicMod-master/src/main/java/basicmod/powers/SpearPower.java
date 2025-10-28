package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.util.ArrayList;

public class SpearPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:SpearPower";
    public static final String NAME = "矛";
    public static final String[] DESCRIPTIONS = {"每次打出单体攻击牌时，移除一名敌方角色的格挡。"};

    public SpearPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
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
        if (card.type == AbstractCard.CardType.ATTACK && card.target == AbstractCard.CardTarget.ENEMY) {
            // 获取随机单个敌人
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(true);
            if (target != null && target.currentBlock > 0) {
                addToBot(new LoseBlockAction(target, owner, target.currentBlock));
            }
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new SpearPower(owner);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
