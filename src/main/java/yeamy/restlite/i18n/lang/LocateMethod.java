package yeamy.restlite.i18n.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

public class LocateMethod extends AbstractMethod {
    final String locate;
    private final ArrayList<Component> data;

    public LocateMethod(String locate, String name, String file, int line, String text, int from) throws LangException {
        super(name, new LinkedHashMap<>());
        this.locate = locate;
        this.data = data(file, line, text, from);
        LinkedHashMap<String, Param> params = this.params;
        data.forEach(e -> {
            if (e instanceof Param p) {
                params.put(p.name, p);
            }
        });
    }

    private static ArrayList<Component> data(String file, int line, String text, int from) throws LangException {
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
            Param param = Param.parse(file, line, text, begin, end);
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
        b.append("    case \"").append(locate).append("\":\n");
        if (data.size() == 1) {
            b.append("        return ");
            data.get(0).createSource(b);
            b.append("\n");
        } else {
            for (Component li : data) {
                if (li instanceof Param p) {
                    p.createSource(b);
                } else {
                    b.append("        _b.WriteString(");
                    li.createSource(b);
                    b.append(")\n");
                }
            }
            b.append("        break\n");
        }
    }

    @Override
    public Set<String> imports() {
        Set<String> imp = Collections.emptySet();
        for (int i = 0; i < data.size(); i++) {
            Set<String> tmp = data.get(i).imports();
            if (tmp.size() == 2) {
                return tmp;
            } else if (tmp.size() > imp.size()) {
                imp = tmp;
            }
        }
        return imp;
    }

}
