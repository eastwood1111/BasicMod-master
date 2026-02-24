package basicmod.util;

public class Sounds {
    // 这里的 MOD_ID 必须和你 resources 文件夹下的名字一致
    private static final String MOD_ID = "soul-five";
    private static final String AUDIO_PATH = MOD_ID + "/audio/";

    // 方式 A：直接指定完整路径
    public static String TEST_SOUND = AUDIO_PATH + "test.wav";

    // 方式 B：如果你在 BasicMod 里使用了自动加载逻辑，
    // 确保这个变量名 "ding" 对应音频文件名 "ding.wav" 或 "ding.ogg"
    public static String ding;
}