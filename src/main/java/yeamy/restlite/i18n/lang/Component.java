package yeamy.restlite.i18n.lang;

public interface Component {
    void createJavaSource(StringBuilder b);

    void createKotlinSource(StringBuilder b);
}