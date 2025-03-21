package yeamy.restlite.i18n.lang;

import java.util.LinkedHashMap;
import java.util.Set;

public abstract class AbstractMethod {
    public final String name;
    protected final LinkedHashMap<String, Param> params;

    protected AbstractMethod(String name, LinkedHashMap<String, Param> params) {
        this.name = name;
        this.params = params;
    }

    abstract void createSource(StringBuilder b);

    public String name() {
        return name;
    }

    public abstract Set<String> imports();
}
