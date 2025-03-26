package yeamy.restlite.i18n.lang;

import java.util.Collection;

public abstract class AbstractFile<T extends AbstractMethod> {
    public final String name;

    public final Collection<T> methods;

    public AbstractFile(String name, Collection<T> methods) {
        this.name = name;
        this.methods = methods;
    }

    public abstract String createSource();

}
