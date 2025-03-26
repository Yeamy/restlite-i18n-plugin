package yeamy.restlite.i18n.lang;

import java.util.Collection;
import java.util.HashMap;

public class InterfaceFile extends AbstractFile<InterfaceMethod> {
    private Configuration conf;
    private final HashMap<String, InterfaceMethod> map = new HashMap<>();

    public InterfaceFile(Configuration conf, Collection<InterfaceMethod> methods) {
        super(conf.getInterface(), methods);
        this.conf = conf;
        methods.forEach(m -> map.put(m.name, m));
    }

    @Override
    public String createSource() {
        StringBuilder b = new StringBuilder();
        b.append("export default interface ").append(name).append(" {\n");
        methods.forEach(method -> method.createSource(b));
        b.setCharAt(b.length() - 1, '}');
        return b.toString();
    }

    public InterfaceMethod get(String name) {
        return map.get(name);
    }
}
