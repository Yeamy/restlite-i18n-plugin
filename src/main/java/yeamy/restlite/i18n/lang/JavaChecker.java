package yeamy.restlite.i18n.lang;

import java.util.Arrays;
import java.util.HashSet;

public class JavaChecker {

    private static final HashSet<String> KEY_WORDS = new HashSet<>();

    static {
        String[] kws = {
                "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const", "continue",
                "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "goto", "if",
                "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package",
                "private", "protected", "public", "record", "return", "strictfp", "short", "static", "super", "switch",
                "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while"
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
