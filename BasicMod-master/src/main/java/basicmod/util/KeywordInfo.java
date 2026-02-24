package basicmod.util;

import basemod.helpers.KeywordColorInfo;

public class KeywordInfo {
    public String ID = "";
    public String PROPER_NAME;
    public String DESCRIPTION;
    public String[] NAMES;
    public String[] EXTRA = new String[] {};
    public KeywordColorInfo COLOR;

    public KeywordInfo() {
    }

    public void prep() {
        if (NAMES == null) return;

        for (int i = 0; i < NAMES.length; i++) {
            if (NAMES[i] == null) continue;

            // 只对包含英文字母的名字做 lower-case，避免影响中文/特殊字符
            boolean hasAsciiLetter = false;
            for (int j = 0; j < NAMES[i].length(); j++) {
                char c = NAMES[i].charAt(j);
                if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                    hasAsciiLetter = true;
                    break;
                }
            }
            if (hasAsciiLetter) {
                NAMES[i] = NAMES[i].toLowerCase(java.util.Locale.ROOT);
            }
        }
    }
}