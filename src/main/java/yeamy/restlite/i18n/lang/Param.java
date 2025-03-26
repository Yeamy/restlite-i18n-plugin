package yeamy.restlite.i18n.lang;

public class Param implements Component{
    public final String type, name;

    private Param(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static Param parse(String file, int line, String text, int begin, int end) throws LangException {
        int s = begin;
        int e = end;
        //trim
        while (s <= e) {
            char c = text.charAt(s);
            if (c != ' ' && c != '\t') {
                if (ArkTSChecker.firstCharValid(c)) {
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
        String str = text.substring(s, m);
        String type = switch (str) {
            case "bool" -> "boolean";
            case "num" -> "number";
            case "str" -> "string";
            case "obj" -> "Object";
            default -> null;
        };
        while (true) {
            char c = text.charAt(m);
            if (c != ' ' && c != '\t') {
                if (ArkTSChecker.firstCharValid(c)) {
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
        if (name.indexOf(' ') != -1 || name.indexOf('\t') != -1 || ArkTSChecker.nameInValid(name)) {
            throw LangException.invalidParamName(file, line, text.substring(begin, end));
        }
        return new Param(type, name);
    }

    @Override
    public void createSource(StringBuilder sb) {
        sb.append(name);
    }
}
