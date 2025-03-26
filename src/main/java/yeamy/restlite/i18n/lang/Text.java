package yeamy.restlite.i18n.lang;

public class Text implements Component {
    public final String text;

    public Text(String text) {
        this.text = text;
    }

    @Override
    public void createJavaSource(StringBuilder b) {
        b.append('"');
        char[] cs = text.toCharArray();
        for (int i = 0, max = cs.length - 1; i <= max; i++) {
            char c = cs[i];
            if (c == '#') {
                if (i + 2 < max && cs[i + 1] == '#' && cs[i + 2] == '{') {
                    continue;
                }
            } else if (c == '"') {
                b.append('\\');
            } else if (c == '\\') {
                if (i + 2 <= max && cs[i + 1] == '\\' && (cs[i + 2] == 'n' || cs[i + 2] == 'r')) {
                    b.append("\\\\\\\\").append(cs[i + 2]);
                    i += 2;
                    continue;
                } else if (i + 1 < max && cs[i + 1] != 'n' && cs[i + 1] != 'r') {
                    b.append("\\");
                }
            }
            b.append(c);
        }
        b.append('"');
    }

    @Override
    public void createKotlinSource(StringBuilder b) {
        char[] cs = text.toCharArray();
        for (int i = 0, max = cs.length - 1; i <= max; i++) {
            char c = cs[i];
            if (c == '#') {
                if (i + 2 < max && cs[i + 1] == '#' && cs[i + 2] == '{') {
                    continue;
                }
            } else if (c == '"' || c == '$') {
                b.append('\\');
            } else if (c == '\\') {
                if (i + 2 <= max && cs[i + 1] == '\\' && (cs[i + 2] == 'n' || cs[i + 2] == 'r')) {
                    b.append("\\\\\\\\").append(cs[i + 2]);
                    i += 2;
                    continue;
                } else if (i + 1 < max && cs[i + 1] != 'n' && cs[i + 1] != 'r') {
                    b.append("\\");
                }
            }
            b.append(c);
        }
    }

}