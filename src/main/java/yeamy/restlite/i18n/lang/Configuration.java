package yeamy.restlite.i18n.lang;

public class Configuration {
    private final String name, util, def;

    public Configuration(String name, String util, String def) throws LangException {
        if (name == null) {
            throw new LangException("\"name\" not defined");
        }
        this.name = name.trim();
        this.util = util.trim();
        this.def = def.trim();
    }

    public String getName() {
        return name;
    }

    public String getInterface() {
        return name;
    }

    public String getUtil() {
        return util;
    }

    public String getName(String locate) {
        return name + locate.substring(0, 1).toUpperCase()
                + locate.replace("-", "").substring(1);
    }

    public String getDefault() {
        return def;
    }

    public String getDefaultFieldName() {
        return getFieldName(def);
    }

    public String getFieldName(String locate) {
        return locate.replace("-", "");
    }
}
