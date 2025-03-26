package yeamy.restlite.i18n.lang;

import java.util.ArrayList;
import java.util.Collections;

public class UtilFile extends AbstractFile<AbstractMethod> {
    private final Configuration conf;
    private final ArrayList<LocateFile> locates = new ArrayList<>();
    private final LocateFile defaultLocate;

    public UtilFile(Configuration conf, LocateFile defaultLocate) {
        super(conf.getUtil(), Collections.emptyList());
        this.conf = conf;
        this.defaultLocate = defaultLocate;
    }

    public void addLocate(LocateFile locate) {
        locates.add(locate);
    }

    public ArrayList<LocateFile> getLocates() {
        return locates;
    }

    @Override
    public String createSource() {
        StringBuilder b = new StringBuilder();
        String ifn = conf.getInterface();
        b.append("import ").append(ifn).append(" from './").append(ifn).append("';\n");
        locates.forEach(l ->
                b.append("import { ").append(l.name).append(" } from './").append(l.name).append("';\n")
        );
        b.append("import { intl } from '@kit.LocalizationKit';\n\nexport default class ").append(name).append(" {\n");
        b.append("  public static readonly DEFAULT_LOCATE = \"").append(conf.getDefault()).append("\";\n");
        locates.forEach(l -> b.append("  public static readonly ").append(l.fieldName).append(": ")
                .append(l.name).append(" = new ").append(l.name).append("();\n"));

        b.append("  public static readonly defaultLocate: ").append(ifn).append(" = ").append(name).append('.')
                .append(defaultLocate.fieldName).append(";\n");

        b.append("  private static readonly map: Map<string, ").append(ifn).append("> = new Map();\n  static {\n");
        locates.forEach(l -> b.append("    ").append(name).append(".map.set(\"").append(l.locate)
                .append("\", ").append(name).append(".").append(l.fieldName).append(");\n"));
        b.append("  }\n\n");
        //TODO
        b.append(String.format("""
                  public static get(): %s {
                    let l = new intl.Locale();
                    let out = %s.map.get(l.language + '-' + l.region);
                    return out == undefined ? %s.%s : out;
                  }
                
                  public static getByLocate(locate: string): %s | undefined {
                    return %s.map.get(locate);
                  }
                
                """, ifn, name, name, conf.getDefaultFieldName(), name, name));

        b.setCharAt(b.length() - 1, '}');
        return b.toString();
    }

}
