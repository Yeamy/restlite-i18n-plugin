package yeamy.restlite.i18n.lang;

public class Configuration {
    private final String pkg, file, def;
    private final boolean http;

    public Configuration(String pkg, String file, String def, String http) throws LangException {
        if (pkg == null) {
            throw new LangException("\"package\" not defined");
        }
        this.pkg = pkg.trim();
        if (file == null) {
            throw new LangException("\"file\" not defined");
        }
        this.file = file.trim();
        if (def == null) {
            throw new LangException("\"default\" not defined");
        }
        this.def = def.trim();
        this.http = http != null && "true".equalsIgnoreCase(http.trim());
    }

    public String getPackage() {
        return pkg;
    }

    public String getFileName() {
        return file;
    }

    public String getDefault() {
        return def;
    }

    public boolean supportHttp() {
        return http;
    }
}
