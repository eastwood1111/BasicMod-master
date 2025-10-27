package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AttackWeakPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:AttackWeakPower";
    public static final String NAME = "攻击虚弱";
    public static final String[] DESCRIPTIONS = {
            "每次打出攻击牌，赋予敌人 #b 层虚弱。"
    };

    private final int weakPerAttack = 1;
    private final boolean affectAll;

    public AttackWeakPower(AbstractCreature owner, boolean affectAll) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.affectAll = affectAll;

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
        if (card.type != AbstractCard.CardType.ATTACK) return; // 只对攻击牌触发

        if (affectAll) {
            for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
                if (!mon.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(mon, owner, new WeakPower(mon, weakPerAttack, false), weakPerAttack));
                }
            }
        } else {
            AbstractMonster mon = AbstractDungeon.getMonsters().getRandomMonster(true);
            if (mon != null && !mon.isDeadOrEscaped()) {
                addToBot(new ApplyPowerAction(mon, owner, new WeakPower(mon, weakPerAttack, false), weakPerAttack));
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        if (affectAll) {
            this.description += " (升级后作用全体敌人)";
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new AttackWeakPower(owner, affectAll);
    }
}
