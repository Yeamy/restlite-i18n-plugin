package yeamy.restlite.i18n.lang;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public abstract class AbstractFile<T extends AbstractMethod> {
    public final String pkg, name;

    public final Collection<T> methods;

    public AbstractFile(String pkg, String name, Collection<T> methods) {
        this.pkg = pkg;
        this.name = name;
        this.methods = methods;
    }

    public abstract String javaSource();

    public abstract String kotlinSource();
}
