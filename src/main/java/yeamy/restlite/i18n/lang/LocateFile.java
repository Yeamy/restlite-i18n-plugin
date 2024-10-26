package yeamy.restlite.i18n.lang;

import java.util.Collection;

public class LocateFile extends AbstractFile<LocateMethod> {
    public final String locate;
    public final String fieldName;
    private final Configuration conf;

    public LocateFile(Configuration conf, String locate, Collection<LocateMethod> methods) {
        super(conf.getPackage(), conf.getName(locate), methods);
        this.conf = conf;
        this.locate = locate;
        this.fieldName = locate.replace("-", "");
    }

    @Override
    public String javaSource() {
        StringBuilder b = new StringBuilder();
        if (pkg.length() > 0) {
            b.append("package ").append(pkg).append(";");
        }
        b.append("public class ").append(name).append(" implements ").append(conf.getInterface()).append(" {");
        for (LocateMethod method : methods) {
            method.createJavaSource(b);
        }
        b.append("}");
        return b.toString();
    }

    @Override
    public String kotlinSource() {
        StringBuilder b = new StringBuilder();
        if (pkg.length() > 0) {
            b.append("package ").append(pkg).append("\n");
        }
        b.append("class ").append(name).append(" : ").append(conf.getInterface()).append(" {");
        for (LocateMethod method : methods) {
            method.createKotlinSource(b);
        }
        b.append("}");
        return b.toString();
    }
}
