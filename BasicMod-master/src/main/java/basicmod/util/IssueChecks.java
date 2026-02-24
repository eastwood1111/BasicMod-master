package basicmod.util;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

/**
 * A class containing utility methods for checking and logging common issues.
 */
@SpireInitializer
public class IssueChecks {
    // 硬编码资源路径，确保检查逻辑不依赖于不稳定的 BasicMod 方法
    private static final String CHAR_PATH = "soul-five/images/character/";

    public static void initialize() {
        characterAssetsCheck();
    }

    private static void characterAssetsCheck() {
        boolean validCharAssets = false;

        // 直接检查硬编码的路径
        FileHandle charResources = new LwjglFileHandle(CHAR_PATH, Files.FileType.Internal);
        if (charResources.exists() && charResources.isDirectory()) {
            validCharAssets = true;
            // 你可以在这里加一行 log 确认路径正确
            // System.out.println("Soul-Five: Character assets found at " + CHAR_PATH);
        } else {
            // System.out.println("Soul-Five: WARNING - Character assets NOT found at " + CHAR_PATH);
        }
    }
}