package yeamy.restlite.i18n.lang;

public class LangException extends Exception {

    public static LangException invalidParamType(String file, int line, String text) {
        return new LangException("Parameter type invalid in file " + file + " at line " + line + " " + text);
    }

    public LangException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
