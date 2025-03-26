package yeamy.restlite.i18n.lang;

import java.util.Collection;
import java.util.LinkedHashMap;

public abstract class AbstractMethod {
	public final String name;
	public final LinkedHashMap<String, Param> params;

	protected AbstractMethod(String name, LinkedHashMap<String, Param> params) {
		this.name = name;
		this.params = params;
	}

	public Collection<Param> params() {
		return params.values();
	}

	public Param get(String name) {
		return params.get(name);
	}

	abstract void createJavaSource(StringBuilder b);

	abstract void createKotlinSource(StringBuilder b);

}
