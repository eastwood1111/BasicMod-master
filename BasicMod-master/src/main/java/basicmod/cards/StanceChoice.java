package basicmod.cards;

import basemod.AutoAdd;
import basicmod.util.CardStats;
import basicmod.util.TextureLoader;
import basicmod.powers.CurrentStancePower;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

@AutoAdd.Ignore
public class StanceChoice extends BaseCard {
    public static final String ID = "StanceChoice";
    private CurrentStancePower.Stance stanceEnum;
    private int amount;

    public StanceChoice(String name, String description, String imgPath, CurrentStancePower.Stance stance, int amount) {
        super(ID, new CardStats(CardColor.COLORLESS, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, -2));
        this.name = name;
        this.rawDescription = description;
        this.stanceEnum = stance;
        this.amount = amount;

        try {
            this.portrait = new TextureAtlas.AtlasRegion(TextureLoader.getTexture(imgPath), 0, 0, 250, 190);
        } catch (Exception e) {
            System.err.println("纹理加载失败: " + imgPath);
        }
        initializeDescription();
    }

    @Override
    public void onChoseThisOption() {
        AbstractPlayer p = AbstractDungeon.player;

        if (!p.hasPower(CurrentStancePower.POWER_ID)) {
            // 如果没有管理器，直接塞一个进去
            p.powers.add(new CurrentStancePower(p));
        }

        // 此时一定有管理器了，直接调用逻辑
        AbstractPower pow = p.getPower(CurrentStancePower.POWER_ID);
        if (pow instanceof CurrentStancePower) {
            ((CurrentStancePower) pow).switchStance(this.stanceEnum, this.amount);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}
    @Override
    public void upgrade() {}
    @Override
    public StanceChoice makeCopy() {
        return new StanceChoice(this.name, this.rawDescription, this.textureImg, this.stanceEnum, this.amount);
    }
}