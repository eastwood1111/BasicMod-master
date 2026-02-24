package basicmod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower; // 易伤
import com.megacrit.cardcrawl.powers.WeakPower;       // 虚弱
import basicmod.powers.BleedPower;
import basicmod.powers.FlamePower;
import basicmod.powers.CurseDeathPower;
import basicmod.charater.MyCharacter;

import static basicmod.BasicMod.makeID;

public class ChaosPotion extends BasePotion {
    public static final String POTION_ID = makeID("ChaosPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public ChaosPotion() {
        // ID, 效力(3), 稀有度(UNCOMMON), 形状(SPHERE), 液体颜色, 混合液颜色, 斑点颜色
        super(POTION_ID, 3, PotionRarity.UNCOMMON, PotionSize.SPHERE, Color.CHARTREUSE.cpy(), Color.BLACK.cpy(), Color.PURPLE.cpy());

        this.playerClass = MyCharacter.Meta.SOUL_FIVE_CLASS;
        this.isThrown = true;
        this.targetRequired = false;
    }

    @Override
    public void use(AbstractCreature target) {
        int amount = this.getPotency();
        AbstractCreature p = AbstractDungeon.player;

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                // 现在有 6 种状态，随机数范围是 0 到 5
                int roll = AbstractDungeon.potionRng.random(5);

                switch (roll) {
                    case 0:
                        // 出血 (BleedPower)
                        this.addToBot(new ApplyPowerAction(m, p, new BleedPower(m, amount), amount));
                        break;
                    case 1:
                        // 炎上 (FlamePower)
                        this.addToBot(new ApplyPowerAction(m, p, new FlamePower(m, p, amount), amount));
                        break;
                    case 2:
                        // 咒死 (CurseDeathPower)
                        this.addToBot(new ApplyPowerAction(m, p, new CurseDeathPower(m, amount), amount));
                        break;
                    case 3:
                        // 中毒 (PoisonPower)
                        this.addToBot(new ApplyPowerAction(m, p, new PoisonPower(m, p, amount), amount));
                        break;
                    case 4:
                        // 易伤 (VulnerablePower) - 构造器: (owner, amount, isSourceMonster)
                        // 注意：第三个参数 isSourceMonster 对于玩家施加通常填 false
                        this.addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, amount, false), amount));
                        break;
                    case 5:
                        // 虚弱 (WeakPower) - 构造器: (owner, amount, isSourceMonster)
                        this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, amount, false), amount));
                        break;
                }
            }
        }
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 3;
    }

    @Override
    public String getDescription() {
        return potionStrings.DESCRIPTIONS[0] + this.getPotency() + potionStrings.DESCRIPTIONS[1];
    }
}