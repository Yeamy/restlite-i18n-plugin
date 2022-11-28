package yeamy.restlite.i18n.lang;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class LocateMethod extends AbstractMethod implements Iterable<Object> {
    private final ArrayList<Object> data;

    public LocateMethod(InterfaceMethod ifm, String fn, int line, String text, int from) throws LangException {
        super(ifm.name, ifm.params);
        this.data = data(ifm, fn, line, text, from);
    }

    public LocateMethod(String name, String fn, int line, String text, int from) throws LangException {
        super(name, new LinkedHashMap<>());
        this.data = data(null, fn, line, text, from);
        LinkedHashMap<String, Param> params = this.params;
        for (Object obj : data) {
            if (obj instanceof Param) {
                Param p = (Param) obj;
                params.put(p.name, p);
            }
        }
    }

    private static ArrayList<Object> data(InterfaceMethod ifm, String fn, int line, String text, int from)
            throws LangException {
        ArrayList<Object> data = new ArrayList<>();
        int split = from;
        while (true) {
            int begin = text.indexOf("#{", from);
            if (begin == -1) {
                if (split != text.length() || data.size() == 0) {
                    data.add(text.substring(split));
                }
                break;
            }
            if (begin > 0 && text.charAt(begin - 1) == '#') {
                from = begin + 2;
                continue;
            }
            begin += 2;
            int end = text.indexOf("}", begin);
            if (end == -1) {
                throw new LangException("Unclosed parameter at line " + line);
            }
            Param param = Param.parse(fn, line, text, begin, end);
            if (ifm != null) {
                Param p = ifm.get(param.name);
                if (p == null) {
                    throw new LangException("Not defined parameter at line " + line + " " + param.name);
                } else if (!p.type.equals(param.type)) {
                    throw new LangException("Parameter type not same with defined at line " + line + " " + param.name);
                }
            }
            if (split != begin - 2) {
                data.add(text.substring(split, begin - 2));
            }
            data.add(param);
            from = split = end + 1;
        }
        return data;
    }

    @NotNull
    @Override
    public Iterator<Object> iterator() {
        return data.iterator();
    }

    @Override
    void createJavaSource(StringBuilder b) {
        b.append("@Override public String ").append(name).append(" (");
        if (params.size() > 0) {
            for (Param p : params()) {
                b.append(p.type).append(" ").append(p.name).append(",");
            }
            b.deleteCharAt(b.length() - 1);
        }
        b.append("){return ");
        if (data.size() == 0) {
            b.append("\"\"");
        } else {
            for (Object li : data) {
                if (li instanceof String) {
                    appendStr(b, li.toString());
                } else {
                    b.append(li);
                }
                b.append('+');
            }
            b.deleteCharAt(b.length() - 1);
        }
        b.append(";}");
    }

    @Override
    void createKotlinSource(StringBuilder b) {
        b.append("override fun ").append(name).append(" (");
        if (params.size() > 0) {
            for (Param p : params()) {
                b.append(p.name).append(": ").append(p.kotlinType()).append(", ");
            }
            b.deleteCharAt(b.length() - 2);
        }
        b.append(") : String = \"");
        if (data.size() == 0) {
            b.append("\"\"");
        } else {
            for (Object li : data) {
                if (li instanceof String) {
                    appendKotlinStr(b, li.toString());
                } else {
                    b.append('$').append(li);
                }
            }
        }
        b.append("\"\n");

    }

    // Invalid escape sequence (valid ones are  \b  \t  \n  \f  \r  \"  \'  \\ )
    private void appendStr(StringBuilder sb, String str) {
        sb.append('"');
        char[] cs = str.toCharArray();
        for (int i = 0, max = cs.length - 1; i <= max; i++) {
            char c = cs[i];
            if (c == '#') {
                if (i + 2 < max && cs[i + 1] == '#' && cs[i + 2] == '{') {
                    continue;
                }
            } else if (c == '"') {
                sb.append('\\');
            } else if (c == '\\') {
                if (i + 2 <= max && cs[i + 1] == '\\' && (cs[i + 2] == 'n' || cs[i + 2] == 'r')) {
                    sb.append("\\\\\\\\").append(cs[i + 2]);
                    i += 2;
                    continue;
                } else if (i + 1 < max && cs[i + 1] != 'n' && cs[i + 1] != 'r') {
                    sb.append("\\");
                }
            }
            sb.append(c);
        }
        sb.append('"');
    }

    private void appendKotlinStr(StringBuilder sb, String str) {
        char[] cs = str.toCharArray();
        for (int i = 0, max = cs.length - 1; i <= max; i++) {
            char c = cs[i];
            if (c == '#') {
                if (i + 2 < max && cs[i + 1] == '#' && cs[i + 2] == '{') {
                    continue;
                }
            } else if (c == '"' || c == '$') {
                sb.append('\\');
            } else if (c == '\\') {
                if (i + 2 <= max && cs[i + 1] == '\\' && (cs[i + 2] == 'n' || cs[i + 2] == 'r')) {
                    sb.append("\\\\\\\\").append(cs[i + 2]);
                    i += 2;
                    continue;
                } else if (i + 1 < max && cs[i + 1] != 'n' && cs[i + 1] != 'r') {
                    sb.append("\\");
                }
            }
            sb.append(c);
        }
    }

}
