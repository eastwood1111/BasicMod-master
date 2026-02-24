package basicmod;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.interfaces.*;
import basicmod.cards.BaseCard;
import basicmod.charater.MyCharacter;
import basicmod.potions.BasePotion;
import basicmod.relics.BaseRelic;
import basicmod.util.GeneralUtils;
import basicmod.util.KeywordInfo;
import basicmod.util.Sounds;
import basicmod.util.TextureLoader;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpireInitializer
public class BasicMod implements
        EditRelicsSubscriber,
        EditCardsSubscriber,
        EditCharactersSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        AddAudioSubscriber,
        PostInitializeSubscriber {

    public static ModInfo info;
    // 1. 強制指定 modID，避免自動抓取失敗
    public static String modID = "soul-five";
    public static final Logger logger = LogManager.getLogger("SoulFive");

    // 2. 修正路徑變量，直接指向你的文件夾名
    private static final String resourcesFolder = "soul-five";

    public static String makeID(String id) {
        return modID + ":" + id;
    }

    public static void initialize() {
        new BasicMod();
        MyCharacter.Meta.registerColor();
    }

    public BasicMod() {
        BaseMod.subscribe(this);
        logger.info(modID + " subscribed to BaseMod.");
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        BaseMod.registerModBadge(badgeTexture, "Soul Five Mod", "Author", "Description", null);
        registerPotions();
    }

    /*----------Localization----------*/

    private static String getLangString() {
        return Settings.language.name().toLowerCase();
    }

    private static final String defaultLanguage = "zhs"; // 建議將默認設為 zhs 方便測試

    @Override
    public void receiveEditStrings() {
        loadLocalization(defaultLanguage);
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            } catch (GdxRuntimeException e) {
                logger.warn("Localization for " + getLangString() + " not found.");
            }
        }
    }

    private void loadLocalization(String lang) {
        // 必须指向 CardStrings.json 而不是 cards.json
        BaseMod.loadCustomStringsFile(CardStrings.class, localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, localizationPath(lang, "CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, localizationPath(lang, "UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String path = localizationPath(defaultLanguage, "keywords.json"); // 确认这里写的是 keywords.json
        FileHandle handle = Gdx.files.internal(path);

        if (handle.exists()) {
            String json = handle.readString(String.valueOf(StandardCharsets.UTF_8));
            KeywordInfo[] keywords = gson.fromJson(json, KeywordInfo[].class);
            if (keywords != null) {
                for (KeywordInfo keyword : keywords) {
                    keyword.prep();
                    registerKeyword(keyword);
                }
            }
        } else {
            // 如果找不到文件，只打印警告而不让游戏崩溃
            logger.warn("未找到关键字定义文件: " + path);
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION);
    }

    /*----------路徑生成器----------*/

    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String imagePath(String file) {
        return resourcesFolder + "/images/" + file;
    }

    public static String relicPath(String file) {
        return resourcesFolder + "/images/relics/" + file;
    }

    // 3. 簡化路徑檢查邏輯，排除包名干擾
    private static String checkResourcesPath() {
        FileHandle resources = new LwjglFileHandle(resourcesFolder, Files.FileType.Internal);
        if (!resources.exists()) {
            throw new RuntimeException("無法找到資源文件夾: resources/" + resourcesFolder);
        }
        return resourcesFolder;
    }

    @Override
    public void receiveEditCharacters() {
        MyCharacter.Meta.registerCharacter();
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd(modID)
                .packageFilter(BaseCard.class)
                .setDefaultSeen(true)
                .cards();
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
                .packageFilter(BaseRelic.class)
                .any(BaseRelic.class, (info, relic) -> {
                    if (relic.pool != null) BaseMod.addRelicToCustomPool(relic, relic.pool);
                    else BaseMod.addRelic(relic, relic.relicType);
                    UnlockTracker.markRelicAsSeen(relic.relicId);
                });
    }

    public static void registerPotions() {
        new AutoAdd(modID)
                .packageFilter(BasePotion.class)
                .any(BasePotion.class, (info, potion) -> {
                    BaseMod.addPotion(potion.getClass(), null, null, null, potion.ID, potion.playerClass);
                });
    }

    @Override public void receiveAddAudio() {}
}