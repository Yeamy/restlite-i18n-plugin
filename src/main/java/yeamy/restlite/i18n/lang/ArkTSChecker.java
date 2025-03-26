package yeamy.restlite.i18n.lang;

import java.util.Arrays;
import java.util.HashSet;

public class ArkTSChecker {

    private static final HashSet<String> KEY_WORDS = new HashSet<>();

    static {
        String[] kws = {
                "let", "const", "var", "new", "type", "interface", "struct", "enum", "public", "private", "protected",
                "class", "extends", "super", "abstract", "function", "return", "async", "await", "import", "export",
                "this", "null", "undefined", "true", "false", "if", "else", "switch", "case", "break", "default", "for",
                "in", "do", "while", "continue"
        };
        KEY_WORDS.addAll(Arrays.asList(kws));
    }

    public static boolean nameInValid(String name) {
        return KEY_WORDS.contains(name) || "setLocate".equals(name);
    }

    public static boolean firstCharValid(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || c == '$';
    }
}
