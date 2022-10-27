package yeamy.restlite.i18n.lang;

public class Configuration {
    private final String pkg, name, proxy, def;
    private boolean servlet, restlite;

    public Configuration(String pkg, String name, String proxy, String def, String servlet, String restlite) throws LangException {
        if (pkg == null) {
            throw new LangException("\"package\" not defined");
        }
        this.pkg = pkg.trim();
        if (name == null) {
            throw new LangException("\"name\" not defined");
        }
        this.name = name.trim();
        this.proxy = (proxy == null) ? (name + "Proxy") : proxy;
        if (def == null) {
            throw new LangException("\"default\" not defined");
        }
        this.def = def.trim();
        if (servlet != null) {
            this.servlet = Boolean.parseBoolean(servlet.trim());
        }
        if (restlite != null) {
            this.restlite = Boolean.parseBoolean(restlite.trim());
        }
    }

    public String getPackage() {
        return pkg;
    }

    public String getProxy() {
        return proxy;
    }

    public String getInterface() {
        return name;
    }

    public String getName(String locate) {
        return name + locate.substring(0, 1).toUpperCase()
                + locate.replace("-", "").substring(1);
    }

    public String getDefault() {
        return def;
    }

    public boolean supportServlet() {
        return restlite || servlet;
    }

    public boolean supportRestLite() {
        return restlite;
    }

}
