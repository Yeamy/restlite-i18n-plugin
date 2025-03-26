package yeamy.restlite.i18n.lang;

public class Param implements Component{
    public final String type, name;

    private Param(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String kotlinType() {
        return switch (type) {
            case "int" -> "Int";
            case "long" -> "Long";
            case "short" -> "Short";
            case "char" -> "Char";
            case "boolean" -> "Boolean";
            case "float" -> "Float";
            case "double" -> "Double";
            case "String" -> type;
            default -> "Any";
        };
    }

    @Override
    public String toString() {
        return name;
    }

    public static Param parse(String file, int line, String text, int begin, int end) throws LangException {
        int s = begin;
        int e = end;
        //trim
        while (s <= e) {
            char c = text.charAt(s);
            if (c != ' ' && c != '\t') {
                if (JavaChecker.firstCharValid(c)) {
                    break;
                } else {
                    throw LangException.invalidParamName(file, line, text.substring(begin, end));
                }
            }
            s++;
        }
        while (e >= s) {
            e--;
            char c = text.charAt(e);
            if (c != ' ' && c != '\t') {
                e++;
                break;
            }
        }
        if (e == s) {
            throw new LangException("Parameter name is empty in file " + file + " at line " + line + " "
                    + text.substring(begin, end));
        }
        //split
        int m = s;
        while (true) {
            char c = text.charAt(m);
            if (c == ' ' || c == '\t') {
                break;
            } else if (m == e) {
                break;
            }
            m++;
        }
        if (m == e) {
            return new Param("Object", text.substring(s, e));
        }
        String type;
        String str = text.substring(s, m);
        type = switch (str) {
            case "str" -> "String";
            case "bool" -> "boolean";
            case "double", "float", "long", "int", "short", "char" -> str;
            default -> null;
        };
        while (true) {
            char c = text.charAt(m);
            if (c != ' ' && c != '\t') {
                if (JavaChecker.firstCharValid(c)) {
                    break;
                } else {
                    throw LangException.invalidParamName(file, line, text.substring(begin, end));
                }
            } else if (m == e) {
                return new Param("Object", str);
            }
            m++;
        }
        if (type == null) {
            throw new LangException("Parameter type invalid in file " + file + " at line " + line + " "
                    + text.substring(s, m));
        }
        String name = text.substring(m, e);
        if (name.indexOf(' ') != -1 || name.indexOf('\t') != -1 || JavaChecker.nameInValid(name)) {
            throw LangException.invalidParamName(file, line, text.substring(begin, end));
        }
        return new Param(type, name);
    }
}
