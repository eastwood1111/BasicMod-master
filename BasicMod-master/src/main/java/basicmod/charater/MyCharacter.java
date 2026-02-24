package basicmod.charater;

import basemod.BaseMod;
import basemod.abstracts.CustomEnergyOrb;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import basicmod.cards.skill.Defend;
import basicmod.cards.attack.Strike;
import basicmod.cards.skill.ShieldStanceCard;
import basicmod.cards.skill.SwordStanceCard;
import basicmod.relics.MyRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import java.util.ArrayList;

public class MyCharacter extends CustomPlayer {
    // 硬編碼路徑，確保與資源文件夾名一致
    private static final String MOD_ID = "soul-five";
    private static final String RES_PATH = MOD_ID + "/images/character/";
    private static final String ID = MOD_ID + ":CharacterID";

    public static final int ENERGY_PER_TURN = 3;
    public static final int MAX_HP = 75;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    private static String[] getNames() {
        if (CardCrawlGame.languagePack.getCharacterString(ID) == null) return new String[]{"小騎士", "小騎士"};
        return CardCrawlGame.languagePack.getCharacterString(ID).NAMES;
    }

    private static String[] getText() {
        if (CardCrawlGame.languagePack.getCharacterString(ID) == null) return new String[]{"描述缺失", "心臟描述缺失", "吸血鬼描述缺失"};
        return CardCrawlGame.languagePack.getCharacterString(ID).TEXT;
    }

    public static class Meta {
        @SpireEnum
        public static PlayerClass SOUL_FIVE_CLASS;
        @SpireEnum(name = "SOUL_FIVE_GRAY_COLOR")
        public static AbstractCard.CardColor CARD_COLOR;
        @SpireEnum(name = "SOUL_FIVE_GRAY_COLOR")
        public static CardLibrary.LibraryType LIBRARY_COLOR;

        // 直接拼寫路徑
        private static final String CHAR_SELECT_BUTTON = "soul-five/images/character/select/button.png";
        private static final String CHAR_SELECT_PORTRAIT = "soul-five/images/character/select/portrait.png";

        private static final String BG_ATTACK = "soul-five/images/character/cardback/bg_attack.png";
        private static final String BG_ATTACK_P = "soul-five/images/character/cardback/bg_attack_p.png";
        private static final String BG_SKILL = "soul-five/images/character/cardback/bg_skill.png";
        private static final String BG_SKILL_P = "soul-five/images/character/cardback/bg_skill_p.png";
        private static final String BG_POWER = "soul-five/images/character/cardback/bg_power.png";
        private static final String BG_POWER_P = "soul-five/images/character/cardback/bg_power_p.png";
        private static final String ENERGY_ORB = "soul-five/images/character/cardback/energy_orb.png";
        private static final String ENERGY_ORB_P = "soul-five/images/character/cardback/energy_orb_p.png";
        private static final String SMALL_ORB = "soul-five/images/character/cardback/small_orb.png";

        private static final Color cardColor = new Color(128f/255f, 128f/255f, 128f/255f, 1f);

        public static void registerColor() {
            BaseMod.addColor(CARD_COLOR, cardColor,
                    BG_ATTACK, BG_SKILL, BG_POWER, ENERGY_ORB,
                    BG_ATTACK_P, BG_SKILL_P, BG_POWER_P, ENERGY_ORB_P,
                    SMALL_ORB);
        }

        public static void registerCharacter() {
            BaseMod.addCharacter(new MyCharacter(), CHAR_SELECT_BUTTON, CHAR_SELECT_PORTRAIT, SOUL_FIVE_CLASS);
        }
    }

    public MyCharacter() {
        super(getNames()[0], Meta.SOUL_FIVE_CLASS,
                new CustomEnergyOrb(
                        new String[]{
                                "soul-five/images/character/energyorb/layer1.png", "soul-five/images/character/energyorb/layer2.png",
                                "soul-five/images/character/energyorb/layer3.png", "soul-five/images/character/energyorb/layer4.png",
                                "soul-five/images/character/energyorb/layer5.png", "soul-five/images/character/energyorb/cover.png",
                                "soul-five/images/character/energyorb/layer1d.png", "soul-five/images/character/energyorb/layer2d.png",
                                "soul-five/images/character/energyorb/layer3d.png", "soul-five/images/character/energyorb/layer4d.png",
                                "soul-five/images/character/energyorb/layer5d.png"
                        },
                        "soul-five/images/character/energyorb/vfx.png",
                        new float[]{-20.0F, 20.0F, -40.0F, 40.0F, 360.0F}
                ),
                new SpriterAnimation("soul-five/images/character/animation/default.scml")
        );

        initializeClass(null, "soul-five/images/character/shoulder2.png", "soul-five/images/character/shoulder.png", "soul-five/images/character/corpse.png",
                getLoadout(), 20.0F, -20.0F, 200.0F, 250.0F, new EnergyManager(ENERGY_PER_TURN));
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            retVal.add(Strike.ID);
            retVal.add(Defend.ID);
        }
        retVal.add(ShieldStanceCard.ID);
        retVal.add(SwordStanceCard.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(MyRelic.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(getNames()[0], getText()[0], MAX_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public AbstractCard getStartCardForEvent() { return new Strike(); }
    @Override
    public int getAscensionMaxHPLoss() { return 4; }
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() { return new AbstractGameAction.AttackEffect[]{AbstractGameAction.AttackEffect.SLASH_VERTICAL, AbstractGameAction.AttackEffect.SLASH_HEAVY}; }
    @Override
    public Color getCardRenderColor() { return Color.LIGHT_GRAY; }
    @Override
    public Color getCardTrailColor() { return Color.LIGHT_GRAY; }
    @Override
    public Color getSlashAttackColor() { return Color.LIGHT_GRAY; }
    @Override
    public BitmapFont getEnergyNumFont() { return FontHelper.energyNumFontRed; }
    @Override
    public void doCharSelectScreenSelectEffect() { CardCrawlGame.sound.playA("ATTACK_DAGGER_2", MathUtils.random(-0.2F, 0.2F)); }
    @Override
    public String getCustomModeCharacterButtonSoundKey() { return "ATTACK_DAGGER_2"; }
    @Override
    public String getLocalizedCharacterName() { return getNames()[0]; }
    @Override
    public String getTitle(PlayerClass playerClass) { return getNames()[1]; }
    @Override
    public String getSpireHeartText() { return getText()[1]; }
    @Override
    public String getVampireText() { return getText()[2]; }
    @Override
    public AbstractCard.CardColor getCardColor() { return Meta.CARD_COLOR; }
    @Override
    public AbstractPlayer newInstance() { return new MyCharacter(); }
}