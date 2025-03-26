package yeamy.restlite.i18n.lang;

public class UtilMethod extends AbstractMethod {

    /**
     * @param dlm default locate method
     */
    public UtilMethod(LocateMethod dlm) {
        super(dlm.name, dlm.params);
    }

    @Override
    public void createJavaSource(StringBuilder b) {
        b.append("    public abstract String ").append(name).append("(");
        if (params.size() > 0) {
            for (Param p : params()) {
                b.append(p.type).append(" ").append(p.name).append(", ");
            }
            b.delete(b.length() - 2, b.length());
        }
        b.append(");\n\n");
    }

    @Override
    void createKotlinSource(StringBuilder b) {
        b.append("    abstract fun ").append(name).append("(");
        if (params.size() > 0) {
            for (Param p : params()) {
                b.append(p.name).append(": ").append(p.kotlinType()).append(", ");
            }
            b.delete(b.length() - 2, b.length());
        }
        b.append("): String\n");

    }
}
