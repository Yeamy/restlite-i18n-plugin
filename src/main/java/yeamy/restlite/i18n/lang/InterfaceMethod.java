package yeamy.restlite.i18n.lang;

public class InterfaceMethod extends AbstractMethod {

    public InterfaceMethod(LocateMethod lm) {
        super(lm.name, lm.params);
    }

    @Override
    void createSource(StringBuilder b) {
        b.append("  ").append(name).append('(');
        if (params.size() > 0) {
            for (Param p : params()) {
                b.append(p.name).append(": ").append(p.type).append(", ");
            }
            b.delete(b.length() - 2, b.length());
        }
        b.append("): string \n\n");
    }
}
