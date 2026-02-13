package basicmod.powers;

import basemod.ReflectionHacks;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class StunPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "basicmod:StunPower";
    public static final String NAME = "击晕";

    // 保存怪物原本准备好的动作（用于解除后继续原招）
    private boolean saved = false;
    private byte savedNextMove;
    private AbstractMonster.Intent savedIntent;
    private String savedMoveName;
    private Object savedMoveInfo; // AbstractMonster$EnemyMoveInfo（私有内部类，用 Object 保存）

    public StunPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/StunPower.png"),
                0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/StunPower.png"),
                0, 0, 32, 32
        );

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        if (!(owner instanceof AbstractMonster)) return;
        AbstractMonster m = (AbstractMonster) owner;

        // 只保存一次（避免叠层时反复覆盖“原招”）
        if (!saved) {
            saved = true;
            savedNextMove = (byte) ReflectionHacks.getPrivate(m, AbstractMonster.class, "nextMove");
            savedIntent   = (AbstractMonster.Intent) ReflectionHacks.getPrivate(m, AbstractMonster.class, "intent");
            savedMoveName = (String) ReflectionHacks.getPrivate(m, AbstractMonster.class, "moveName");
            savedMoveInfo = ReflectionHacks.getPrivate(m, AbstractMonster.class, "move"); // EnemyMoveInfo
        }

        // 显示眩晕意图（这里 moveId 仅用于显示/占位，关键是解除时恢复原 move）
        m.setMove((byte) -2, AbstractMonster.Intent.STUN);
        m.createIntent();
    }

    @Override
    public void atEndOfRound() {
        if (this.amount <= 1) {
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(this.owner, this.owner, this.ID)
            );
        } else {
            this.amount--;
        }
    }

    @Override
    public void onRemove() {
        if (!(owner instanceof AbstractMonster)) return;
        AbstractMonster m = (AbstractMonster) owner;
        if (m.isDeadOrEscaped()) return;
        if (!saved) return;

        // 用 action 排队，避免与移除/回合结算时序冲突
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                if (m.isDeadOrEscaped()) { isDone = true; return; }

                // 恢复“原本准备好的那一招”
                ReflectionHacks.setPrivate(m, AbstractMonster.class, "nextMove", savedNextMove);
                ReflectionHacks.setPrivate(m, AbstractMonster.class, "intent", savedIntent);
                ReflectionHacks.setPrivate(m, AbstractMonster.class, "moveName", savedMoveName);
                ReflectionHacks.setPrivate(m, AbstractMonster.class, "move", savedMoveInfo);

                m.createIntent();
                m.applyPowers();
                isDone = true;
            }
        });
    }

    @Override
    public void updateDescription() {
        this.description = "无法行动。";
    }

    @Override
    public AbstractPower makeCopy() {
        return new StunPower(owner, amount);
    }
}
