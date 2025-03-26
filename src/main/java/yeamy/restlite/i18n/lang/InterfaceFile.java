package yeamy.restlite.i18n.lang;

import java.util.Collection;
import java.util.HashMap;

public class InterfaceFile extends AbstractFile<InterfaceMethod> {
    private final HashMap<String, InterfaceMethod> map = new HashMap<>();

    public InterfaceFile(Configuration conf, Collection<InterfaceMethod> methods) {
        super(conf.getPackage(), conf.getInterface(), methods);
        methods.forEach(m -> map.put(m.name, m));
    }

    @Override
    public String javaSource() {
        StringBuilder b = new StringBuilder();
        if (pkg.length() > 0) {
            b.append("package ").append(pkg).append(";");
        }
        b.append("public interface ").append(name).append(" {");
        for (InterfaceMethod method : methods) {
            method.createJavaSource(b);
        }
        b.append("}");
        return b.toString();
    }

    @Override
    public String kotlinSource() {
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
