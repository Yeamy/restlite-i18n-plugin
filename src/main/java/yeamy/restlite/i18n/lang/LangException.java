package yeamy.restlite.i18n.lang;

public class LangException extends Exception {

    public static LangException invalidParamName(String file, int line, String text) {
        return new LangException("Parameter type invalid in file " + file + " at line " + line + " " + text);
    }

    public LangException(String message) {
        super(message);
    }
}
