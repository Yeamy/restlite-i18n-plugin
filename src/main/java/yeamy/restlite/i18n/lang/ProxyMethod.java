package yeamy.restlite.i18n.lang;

import java.util.Collection;

public class ProxyMethod extends AbstractMethod {

	public ProxyMethod(InterfaceMethod ifm) {
		super(ifm.name, ifm.params);
	}

	@Override
	void createJavaSource(StringBuilder b) {
		b.append("@Override public String ").append(name).append(" (");
		if (params.size() > 0) {
			for (Param p : params()) {
				b.append(p.type).append(" ").append(p.name).append(",");
			}
			b.deleteCharAt(b.length() - 1);
		}
		b.append("){ return impl.").append(name).append("(");
		Collection<Param> params = params();
		if (params.size() > 0) {
			for (Param param : params) {
				b.append(param.name).append(",");
			}
			b.deleteCharAt(b.length() - 1);
		}
		b.append(");}");
	}

}
