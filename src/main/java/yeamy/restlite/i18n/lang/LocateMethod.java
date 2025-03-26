package yeamy.restlite.i18n.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LocateMethod extends AbstractMethod {
    private final ArrayList<Component> data;

    public LocateMethod(InterfaceMethod ifm, String fn, int line, String text, int from) throws LangException {
        super(ifm.name, ifm.params);
        this.data = data(ifm, fn, line, text, from);
    }

    public LocateMethod(String name, String fn, int line, String text, int from) throws LangException {
        super(name, new LinkedHashMap<>());
        this.data = data(null, fn, line, text, from);
        data.forEach(obj -> {
            if (obj instanceof Param p) {
                this.params.put(p.name, p);
            }
        });
    }

    private static ArrayList<Component> data(InterfaceMethod ifm, String fn, int line, String text, int from)
            throws LangException {
        ArrayList<Component> data = new ArrayList<>();
        int split = from;
        while (true) {
            int begin = text.indexOf("#{", from);
            if (begin == -1) {
                if (split != text.length() || data.size() == 0) {
                    data.add(new Text(text.substring(split)));
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
                data.add(new Text(text.substring(split, begin - 2)));
            }
            data.add(param);
            from = split = end + 1;
        }
        return data;
    }

    @Override
    void createSource(StringBuilder b) {
        b.append("  ").append(name).append('(');
        if (params.size() > 0) {
            for (Param p : params()) {
                b.append(p.name).append(": ").append(p.type).append(", ");
            }
            b.delete(b.length() - 2, b.length());
        }
        b.append("): string {\n    return ");
        if (data.size() == 0) {
            b.append("\"\"");
        } else {
            data.forEach(li -> {
                li.createSource(b);
                b.append(" + ");
            });
            b.delete(b.length() - 3, b.length());
        }
        b.append("\n  }\n\n");
    }

}
