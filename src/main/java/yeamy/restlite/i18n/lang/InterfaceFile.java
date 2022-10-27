package yeamy.restlite.i18n.lang;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class InterfaceFile extends AbstractFile<InterfaceMethod> implements Iterable<InterfaceMethod> {
    private final boolean supportRestLite;
    private final HashMap<String, InterfaceMethod> map = new HashMap<>();

    public InterfaceFile(Configuration conf, Collection<InterfaceMethod> methods) {
        super(conf.getPackage(), conf.getInterface(), methods);
        this.supportRestLite = conf.supportRestLite();
        methods.forEach(m -> map.put(m.name, m));
    }

    @Override
    public Iterator<InterfaceMethod> iterator() {
        return methods.iterator();
    }

    @Override
    protected String createSourceCode() {
        StringBuilder b = new StringBuilder("package ").append(pkg).append(";");
//        if (supportRestLite) {
//            b.append("import yeamy.restlite.annotation.Generator;");
//            b.append("@Generator(className = \"").append(pkg).append('.')
//                    .append(conf.getProxy()).append("\", method = \"get\")");
//        }
        b.append("public interface ").append(name).append(" {");
        for (InterfaceMethod method : methods) {
            method.createSourceCode(b);
        }
        b.append("}");
        return b.toString();
    }

    public InterfaceMethod get(String name) {
        return map.get(name);
    }
}
