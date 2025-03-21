package yeamy.restlite.i18n.lang;

import java.util.Collections;
import java.util.Set;

public class Text implements Component {
    public final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void createSource(StringBuilder sb) {
        sb.append('"');
        char[] cs = text.toCharArray();
        for (int i = 0, max = cs.length - 1; i <= max; i++) {
            char c = cs[i];
            if (c == '#') {
                if (i + 2 < max && cs[i + 1] == '#' && cs[i + 2] == '{') {
                    continue;
                }
            } else if (c == '"') {
                sb.append('\\');
            } else if (c == '\\') {
                if (i + 2 <= max && cs[i + 1] == '\\' && (cs[i + 2] == 'n' || cs[i + 2] == 'r')) {
                    sb.append("\\\\\\\\").append(cs[i + 2]);
                    i += 2;
                    continue;
                } else if (i + 1 < max && cs[i + 1] != 'n' && cs[i + 1] != 'r') {
                    sb.append("\\");
                }
            }
            sb.append(c);
        }
        sb.append('"');
    }

    @Override
    public Set<String> imports() {
        return Collections.emptySet();
    }
}
