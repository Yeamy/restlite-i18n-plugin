package yeamy.restlite.i18n.lang;

import java.util.Collection;

public class LocateFile extends AbstractFile<LocateMethod> {
    public final String locate;
    public final String fieldName;
    private final Configuration conf;

    public LocateFile(Configuration conf, String locate, Collection<LocateMethod> methods) {
        super(conf.getName(locate), methods);
        this.conf = conf;
        this.locate = locate;
        this.fieldName = conf.getFieldName(locate);
    }

    @Override
    public String createSource() {
        StringBuilder b = new StringBuilder();
        String ifn = conf.getInterface();
        b.append("import ").append(ifn).append(" from './").append(ifn).append("';\n\n");
        b.append("export class ").append(name).append(" implements ").append(ifn).append(" {\n");
        methods.forEach(method -> method.createSource(b));
        b.setCharAt(b.length() - 1, '}');
        return b.toString();
    }
}
