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
                    data.add(text.substring(split).replace("##{", "#{"));
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
                data.add(text.substring(split, begin - 2).replace("##{", "#{"));
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
    void createSourceCode(StringBuilder b) {
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
                    b.append('"').append(li).append('"');
                } else {
                    b.append(li);
                }
                b.append('+');
            }
            b.deleteCharAt(b.length() - 1);
        }
        b.append(";}");
    }

}