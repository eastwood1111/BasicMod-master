package basicmod.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Locale;

public class TextureLoader {
    private static final HashMap<String, Texture> textures = new HashMap<>();
    public static final Logger logger = LogManager.getLogger(TextureLoader.class.getName());

    // 定义资源根目录，确保这里是 "soul-five"
    private static final String RES_PATH = "soul-five/";

    // 手动实现路径方法，不再从 BasicMod 导入
    private static String imagePath(String path) {
        return RES_PATH + "images/" + path;
    }

    private static String powerPath(String path) {
        return RES_PATH + "images/powers/" + path;
    }

    public static Texture getTexture(final String filePath) {
        return getTexture(filePath, true);
    }

    public static Texture getTexture(final String filePath, boolean linear) {
        if (textures.get(filePath) == null) {
            try {
                loadTexture(filePath, linear);
            } catch (GdxRuntimeException e) {
                logger.info("Failed to find texture " + filePath);
                // 找不到时尝试加载 missing.png
                Texture missing = getTextureNull(imagePath("missing.png"), false);
                return missing;
            }
        }
        Texture t = textures.get(filePath);
        if (t != null && t.getTextureObjectHandle() == 0) {
            textures.remove(filePath);
            t = getTexture(filePath, linear);
        }
        return t;
    }

    public static Texture getTextureNull(final String filePath) {
        return getTextureNull(filePath, true);
    }

    public static Texture getTextureNull(final String filePath, boolean linear) {
        if (!textures.containsKey(filePath)) {
            try {
                loadTexture(filePath, linear);
            } catch (GdxRuntimeException e) {
                textures.put(filePath, null);
            }
        }
        Texture t = textures.get(filePath);
        if (t != null && t.getTextureObjectHandle() == 0) {
            textures.remove(filePath);
            t = getTextureNull(filePath, linear);
        }
        return t;
    }

    public static String getCardTextureString(final String cardName, final AbstractCard.CardType cardType)
    {
        String textureString = imagePath("cards/" + cardType.name().toLowerCase(Locale.ROOT) + "/" + cardName + ".png");

        FileHandle h = Gdx.files.internal(textureString);
        if (!h.exists())
        {
            switch (cardType) {
                case ATTACK:
                    textureString = imagePath("cards/attack/default.png");
                    break;
                case POWER:
                    textureString = imagePath("cards/power/default.png");
                    break;
                default:
                    textureString = imagePath("cards/skill/default.png");
                    break;
            }
        }
        return textureString;
    }

    private static void loadTexture(final String textureString, boolean linearFilter) throws GdxRuntimeException {
        Texture texture = new Texture(textureString);
        if (linearFilter) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        } else {
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        }
        textures.put(textureString, texture);
    }

    public static Texture getPowerTexture(final String powerName) {
        return getTexture(powerPath(powerName + ".png"));
    }

    public static Texture getHiDefPowerTexture(final String powerName) {
        return getTextureNull(powerPath("large/" + powerName + ".png"));
    }
}