package basicmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class StunPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = "basicmod:StunPower";
    public static final String NAME = "击晕";
    public static final String[] DESCRIPTIONS = new String[]{"无法行动。"};

    public StunPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/large/example.png"), 0, 0, 84, 84
        );
        this.region48 = new TextureAtlas.AtlasRegion(
                ImageMaster.loadImage("basicmod/images/powers/example.png"), 0, 0, 32, 32
        );

        updateDescription();
    }

    // 初次施加时，显示击晕意图
    @Override
    public void onInitialApplication() {
        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) owner;
            m.setMove((byte)0, AbstractMonster.Intent.STUN);
            m.createIntent();
        }
    }

    // 回合开始时，怪物本回合不执行攻击动作
    @Override
    public void atStartOfTurn() {
        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) owner;
            // 暂停动作，确保动作序列正确
            addToBot(new WaitAction(0.1f));
            // 不执行任何攻击动作，本回合跳过
        }
    }

    // 回合结束后减少回合数并移除 Power
    @Override
    public void atEndOfRound() {
        this.amount--;
        if (this.amount <= 0) {
            addToBot(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
        }
    }

    // Power 移除时，让怪物恢复正常意图
    @Override
    public void onRemove() {
        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster) owner;
            m.rollMove();     // 重新选择下一步行动
            m.createIntent(); // 刷新 UI
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new StunPower(owner, amount);
    }
}
