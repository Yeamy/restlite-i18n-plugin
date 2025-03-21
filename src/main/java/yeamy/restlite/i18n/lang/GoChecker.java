package yeamy.restlite.i18n.lang;

import java.util.Arrays;
import java.util.HashSet;

public class GoChecker {

    private static final HashSet<String> KEY_WORDS = new HashSet<>();

    static {
        String[] kws = {
                "const", "var", "type", "func", "import", "package", "if", "else", "switch", "case", "default", "for",
                "break", "continue", "goto", "defer", "return", "go", "select", "chan", "struct", "interface", "map",
                "fallthrough", "range", "nil", "bool", "int", "int8", "int16", "int32", "int64", "uint", "uint8",
                "uint16", "uint32", "uint64", "uintptr", "float32", "float64", "complex64", "complex128", "byte",
                "rune", "string", "true", "false", "iota", "append", "cap", "close", "complex", "copy", "delete",
                "imag", "len", "make", "new", "panic", "print", "println", "real", "recover", "any"
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
