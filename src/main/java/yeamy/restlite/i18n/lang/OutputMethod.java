package yeamy.restlite.i18n.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class OutputMethod extends AbstractMethod {
    private final ArrayList<LocateMethod> methods = new ArrayList<>();

    public OutputMethod(LocateMethod lm) {
        super(lm.name, lm.params);
        methods.add(lm);
    }

    @Override
    void createSource(StringBuilder b) {
        b.append("\nfunc ").append(name);
        if (params.size() > 0) {
            b.append("(lang string, ");
            params.values().forEach(p -> b.append(p.name).append(' ').append(p.type).append(", "));
            b.delete(b.length() - 2, b.length());
            b.append(") string {\n    var _b strings.Builder\n    switch lang {\n");
            methods.forEach(m -> m.createSource(b));
            b.append("    }\n    return _b.String()\n}");
        } else {
            b.append("(lang string) string {\n    switch lang {\n");
            methods.forEach(m -> m.createSource(b));
            b.append("    }\n    return \"\"\n}");
        }
    }

    public void add(LocateMethod m) {
        methods.add(m);
    }

    @Override
    public Set<String> imports() {
        Set<String> imp = Collections.emptySet();
        for (int i = 0; i < methods.size(); i++) {
            Set<String> tmp = methods.get(i).imports();
            if (tmp.size() == 2) {
                return tmp;
            } else if (tmp.size() > imp.size()) {
                imp = tmp;
            }
        }
        return imp;
    }
}
