package yeamy.restlite.i18n.lang;

import java.util.LinkedHashMap;

public class InterfaceMethod extends AbstractMethod {

    public static InterfaceMethod create(LocateMethod lm) {
        return new InterfaceMethod(lm.name, lm.params);
    }

    private InterfaceMethod(String name, LinkedHashMap<String, Param> params) {
        super(name, params);
    }

    @Override
    public void createJavaSource(StringBuilder b) {
        b.append("String ").append(name).append(" (");
        if (params.size() > 0) {
            for (Param p : params()) {
                b.append(p.type).append(" ").append(p.name).append(",");
            }
            b.deleteCharAt(b.length() - 1);
        }
        b.append(");");
    }
}
