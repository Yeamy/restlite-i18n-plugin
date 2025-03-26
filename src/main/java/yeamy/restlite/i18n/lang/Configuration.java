package yeamy.restlite.i18n.lang;

public class Configuration {
    public static final String SERVLET_JAVAX = "javax";
    public static final String SERVLET_JAKARTA = "jakarta";
    public static final String SERVLET_RESTLITE = "restlite";
    private final String pkg, name, util, proxy, def;
    public final String servlet;

    public Configuration(String pkg, String name, String util, String proxy, String def, String servlet) throws LangException {
        if (pkg == null) {
            throw new LangException("\"package\" not defined");
        }
        this.pkg = pkg.trim();
        if (name == null) {
            throw new LangException("\"name\" not defined");
        }
        this.name = name.trim();
        if (def == null) {
            throw new LangException("\"default\" not defined");
        }
        this.def = def.trim();
        if (servlet != null) {
            switch (servlet.trim().toLowerCase()) {
                case "true":
                case SERVLET_JAVAX:
                    this.servlet = SERVLET_JAVAX;
                    break;
                case SERVLET_JAKARTA:
                    this.servlet = SERVLET_JAKARTA;
                    break;
                case SERVLET_RESTLITE:
                    this.servlet = SERVLET_RESTLITE;
                    break;
                default:
                    this.servlet = null;
                    break;
            }
        } else {
            this.servlet = null;
        }
        this.util = util;
        this.proxy = proxy;
    }

    public String getPackage() {
        return pkg;
    }

    public String getProxy() {
        return proxy;
    }

    public String getUtil() {
        return util;
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
        return servlet != null;
    }

    public String getJavaLocateModel() {
        return " implements " + name;
    }

    public String getKotlinLocateModel() {
        return " : " + name;
    }

    public boolean hasProxy() {
        return proxy != null && !proxy.isEmpty();
    }

    public boolean hasUtil() {
        return util != null && !util.isEmpty();
    }
}
