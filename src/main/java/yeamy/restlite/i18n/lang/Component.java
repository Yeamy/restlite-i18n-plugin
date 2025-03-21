package yeamy.restlite.i18n.lang;

import java.util.Set;

public interface Component {
    void createSource(StringBuilder sb);

    Set<String> imports();
}
