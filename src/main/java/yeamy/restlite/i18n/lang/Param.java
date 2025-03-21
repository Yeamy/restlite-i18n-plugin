package yeamy.restlite.i18n.lang;

import java.util.Set;

public class Param implements Component {
    private final static Set<String> ONE = Set.of("strings");
    private final static Set<String> TWO = Set.of("strconv", "strings");
    public final String type, name;
    private final int prec;
    private final Set<String> imports;

    private Param(String type, String name, int prec) {
        this.type = type;
        this.name = name;
        this.prec = prec;
        this.imports = switch (type) {
            case "int8", "int16", "int32", "int64", "int", "uint8", "uint16", "uint32", "uint64", "uint", "float64",
                 "bool" -> TWO;
//            case "string","byte" ,"rune" -> Set.of("strings");
            default -> ONE;
        };
    }

    public static Param parse(String file, int line, String text, int begin, int end) throws LangException {
        int s = begin;
        int e = end;
        //trim
        while (s <= e) {
            char c = text.charAt(s);
            if (c != ' ' && c != '\t') {
                if (GoChecker.firstCharValid(c)) {
                    break;
                } else {
                    throw LangException.invalidParamType(file, line, text.substring(begin, end));
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
            return new Param("string", text.substring(s, e), 0);
        }
        String str = text.substring(s, m);
        int prec = 0;
        String type;
        if (str.charAt(0) == 'f') {
            try {
                prec = Integer.parseInt(str.substring(1));
                type = "float64";
            } catch (Exception ex) {
                throw LangException.invalidParamType(file, line, text.substring(begin, end));
            }
        } else {
            type = switch (str) {
                case "str" -> "string";
                case "i8" -> "int8";
                case "i16" -> "int16";
                case "i32" -> "int32";
                case "i64" -> "int64";
                case "u8" -> "uint8";
                case "u16" -> "uint16";
                case "u32" -> "uint32";
                case "u64" -> "uint64";
                case "int", "uint", "bool", "byte", "uintptr", "complex64", "complex128", "rune" -> str;
                default -> null;
            };
        }
        while (true) {
            char c = text.charAt(m);
            if (c != ' ' && c != '\t') {
                if (GoChecker.firstCharValid(c)) {
                    break;
                } else {
                    throw LangException.invalidParamType(file, line, text.substring(begin, end));
                }
            } else if (m == e) {
                throw LangException.invalidParamType(file, line, text.substring(begin, end));
//                return new Param("any", str, prec);
            }
            m++;
        }
        if (type == null) {
            throw new LangException("Parameter type invalid in file " + file + " at line " + line + " "
                    + text.substring(s, m));
        }
        String name = text.substring(m, e);
        if (name.indexOf(' ') != -1 || name.indexOf('\t') != -1 || GoChecker.nameInValid(name)) {
            throw LangException.invalidParamType(file, line, text.substring(begin, end));
        }
        return new Param(type, name, prec);
    }

    @Override
    public void createSource(StringBuilder sb) {
        sb.append("        _b.Write");
        switch (type) {
            case "string" -> sb.append("String(").append(name).append(")\n");
            case "int8" -> sb.append("String(strconv.FormatInt(int64(").append(name).append("), 8))\n");
            case "int16" -> sb.append("String(strconv.FormatInt(int64(").append(name).append("), 16))\n");
            case "int32" -> sb.append("String(strconv.FormatInt(int64(").append(name).append("), 32))\n");
            case "int64" -> sb.append("String(strconv.FormatInt(").append(name).append(", 64))\n");
            case "int" -> sb.append("String(strconv.Itoa(").append(name).append("))\n");
            case "uint8" -> sb.append("String(strconv.FormatUint(uint64(").append(name).append("), 8))\n");
            case "uint16" -> sb.append("String(strconv.FormatUint(uint64(").append(name).append("), 16))\n");
            case "uint32" -> sb.append("String(strconv.FormatUint(uint64(").append(name).append("), 32))\n");
            case "uint64" -> sb.append("String(strconv.FormatUint(").append(name).append(", 64))\n");
            case "uint" -> sb.append("String(strconv.FormatUint(uint64(").append(name).append("), 64))\n");
            case "float64" ->
                    sb.append("String(strconv.FormatFloat(").append(name).append(", 'f', ").append(prec).append(", 64))\n");
            case "bool" -> sb.append("String(strconv.FormatBool(").append(name).append("))\n");
            case "byte" -> sb.append("Byte(").append(name).append(")\n");
            case "rune" -> sb.append("Rune(").append(name).append(")\n");
            default -> sb.delete(sb.length() - "        _b.Write".length(), sb.length());
//          "uintptr", "complex64", "complex128";
        }
    }

    @Override
    public Set<String> imports() {
        return imports;
    }
}
