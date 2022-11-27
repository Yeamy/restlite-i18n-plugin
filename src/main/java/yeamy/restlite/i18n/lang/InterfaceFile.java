package yeamy.restlite.i18n.lang;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class InterfaceFile extends AbstractFile<InterfaceMethod> implements Iterable<InterfaceMethod> {
    private final HashMap<String, InterfaceMethod> map = new HashMap<>();

    public InterfaceFile(Configuration conf, Collection<InterfaceMethod> methods) {
        super(conf.getPackage(), conf.getInterface(), methods);
        methods.forEach(m -> map.put(m.name, m));
    }

    @Override
    public Iterator<InterfaceMethod> iterator() {
        return methods.iterator();
    }

    @Override
    protected String createJavaSource() {
        StringBuilder b = new StringBuilder();
        if (pkg.length() > 0) {
            b.append("package ").append(pkg).append(";");
        }
//        if (supportRestLite) {
//            b.append("import yeamy.restlite.annotation.Generator;");
//            b.append("@Generator(className = \"").append(pkg).append('.')
//                    .append(conf.getProxy()).append("\", method = \"get\")");
//        }
        b.append("public interface ").append(name).append(" {");
        for (InterfaceMethod method : methods) {
            method.createJavaSource(b);
        }
        b.append("}");
        return b.toString();
    }

    @Override
    protected String createKotlinSource() {
        StringBuilder b = new StringBuilder();
        if (pkg.length() > 0) {
            b.append("package ").append(pkg).append('\n');
        }
        b.append("interface ").append(name).append(" {\n");
        for (InterfaceMethod method : methods) {
            method.createKotlinSource(b);
        }
        b.append("}");
        return b.toString();
    }

    public InterfaceMethod get(String name) {
        return map.get(name);
    }
}
