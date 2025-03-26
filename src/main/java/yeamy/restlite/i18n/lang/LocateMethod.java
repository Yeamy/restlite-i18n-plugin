package yeamy.restlite.i18n.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LocateMethod extends AbstractMethod {
    private final ArrayList<Component> data;

    public LocateMethod(String name, LinkedHashMap<String, Param> params, String fileName, int line, String text, int from) throws LangException {
        super(name, params);
        this.data = readData(params, fileName, line, text, from);
    }

    public LocateMethod(String name, String fileName, int line, String text, int from) throws LangException {
        super(name, new LinkedHashMap<>());
        this.data = readData(null, fileName, line, text, from);
        for (Component li : data) {
            if (li instanceof Param p) {
                this.params.put(p.name, p);
            }
        }
    }

    private static ArrayList<Component> readData(LinkedHashMap<String, Param> params, String fileName, int line, String text, int from) throws LangException {
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
            Param param = Param.parse(fileName, line, text, begin, end);
            if (params != null) {
                Param p = params.get(param.name);
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
    void createJavaSource(StringBuilder b) {
        b.append("    @Override\n    public String ").append(name).append(" (");
        if (params.size() > 0) {
            for (Param p : params()) {
                b.append(p.type).append(" ").append(p.name).append(",");
            }
            b.deleteCharAt(b.length() - 1);
        }
        b.append(") {\n        return ");
        if (data.isEmpty()) {
            b.append("\"\"");
        } else {
            data.forEach(li -> {
                li.createJavaSource(b);
                b.append(" + ");
            });
            b.delete(b.length() - 3, b.length());
        }
        b.append(";\n    }\n");
    }

    @Override
    void createKotlinSource(StringBuilder b) {
        b.append("    override fun ").append(name).append(" (");
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
            for (Component li : data) {
                li.createKotlinSource(b);
            }
        }
        b.append("\"\n");

    }

}
