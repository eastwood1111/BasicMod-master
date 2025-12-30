package basicmod.relics;

import basicmod.charater.MyCharacter;
import basicmod.powers.CurrentStancePower; // 引入你的Power
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import static basicmod.BasicMod.makeID;

public class RenGouRelic extends BaseRelic {
    public static final String ID = makeID("RenGouRelic");

    public RenGouRelic() {
        super(ID, "myrelic", MyCharacter.Meta.CARD_COLOR, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        // 1. 视觉效果
        this.flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));

        // 2. 核心逻辑：使用匿名 Action 来处理 Power 的获取和切换
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractPlayer p = AbstractDungeon.player;

                // 第一步：检查玩家是否有这个 Power
                if (!p.hasPower(CurrentStancePower.POWER_ID)) {
                    // 如果没有，立刻手动赋予一个（不走ApplyPowerAction队列，为了立刻生效）
                    CurrentStancePower newPower = new CurrentStancePower(p);
                    p.powers.add(newPower);
                    // 必须调用 onInitialApplication 以确保初始化逻辑触发
                    newPower.onInitialApplication();

                    // 或者更稳妥但稍慢的方法是使用 ApplyPowerAction，但需要把切换逻辑延后
                    // 这里为了代码简洁，直接手动添加是没问题的
                }

                // 第二步：获取 Power 实例并切换架势
                // 使用 getPower 获取对象
                Object powerObj = p.getPower(CurrentStancePower.POWER_ID);

                if (powerObj instanceof CurrentStancePower) {
                    CurrentStancePower stancePower = (CurrentStancePower) powerObj;

                    // *** 关键：调用你写的 switchStance 方法 ***
                    // 参数1: 目标架势 (IRON)
                    // 参数2: 数值 (例如给予 4 层金属化效果) -> 你可以根据需要修改这个数字
                    stancePower.switchStance(CurrentStancePower.Stance.IRON, 9);
                }

                // 动作完成，必须设为 true
                this.isDone = true;
            }
        });
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RenGouRelic();
    }
}
