package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class AttackWeakPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:AttackWeakPower";
    public static final String NAME = "攻击缓慢";
    public static final String[] DESCRIPTIONS = {
            "每次打出攻击牌，对 #b 敌人施加1层缓慢。"
    };

    private final boolean upgraded;
    private final int weakPerAttack = 1;

    public AttackWeakPower(AbstractCreature owner, boolean upgraded) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.upgraded = upgraded;
        this.amount = 0; // 记录触发次数
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
        if (card.type == AbstractCard.CardType.ATTACK) {
            if (upgraded) {
                // 全体敌人
                for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
                    if (!mon.isDeadOrEscaped()) {
                        addToBot(new ApplyPowerAction(mon, owner, new WeakPower(mon, weakPerAttack, false), weakPerAttack));
                    }
                }
            } else {
                // 随机敌人
                AbstractMonster mon = AbstractDungeon.getMonsters().getRandomMonster(true);
                if (mon != null && !mon.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(mon, owner, new WeakPower(mon, weakPerAttack, false), weakPerAttack));
                }
            }
            this.amount += weakPerAttack;
            updateDescription();
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("#b", upgraded ? "所有" : "随机一名");
    }

    @Override
    public AbstractPower makeCopy() {
        return new AttackWeakPower(owner, upgraded);
    }
}
