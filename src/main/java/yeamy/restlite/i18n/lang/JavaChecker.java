package yeamy.restlite.i18n.lang;

import java.util.Arrays;
import java.util.HashSet;

public class JavaChecker {

    private static final HashSet<String> KEY_WORDS = new HashSet<>();

    static {
        String[] kws = {
                "package", "public", "protected", "private", "static", "abstract", "final", "class", "interface",
                "implements", "extends", "enum", "assert", "transient", "null", "try", "catch", "finally", "throw",
                "throws", "if", "else", "switch", "case", "default", "break", "return", "byte", "char", "short", "int",
                "long", "float", "double"};
        KEY_WORDS.addAll(Arrays.asList(kws));
    }

    public static boolean nameInValid(String name) {
        return KEY_WORDS.contains(name) || "setLocate".equals(name);
    }

    public static boolean firstCharValid(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || c == '$';
    }
}
