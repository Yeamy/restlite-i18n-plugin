package yeamy.restlite.i18n.lang;

import java.util.ArrayList;
import java.util.Collection;

public class ProxyFile extends AbstractFile<ProxyMethod> {
    private final Configuration conf;
    private final ArrayList<LocateFile> files;
    private final LocateFile _default;

    public ProxyFile(Configuration conf, Collection<ProxyMethod> methods, ArrayList<LocateFile> files,
                     LocateFile _default) {
        super(conf.getPackage(), conf.getProxy(), methods);
        this.conf = conf;
        this.files = files;
        this._default = _default;
    }

    @Override
    protected String createJavaSource() {
        StringBuilder b = new StringBuilder();
        if (pkg.length() > 0) {
            b.append("package ").append(pkg).append(";");
        }
        if (conf.supportServlet()) {
            b.append("import java.util.*;");
            b.append("import javax.servlet.http.HttpServletRequest;");
        } else {
            b.append("import java.util.HashMap;");
        }
        String ifn = conf.getInterface();
        b.append("public class ").append(name).append(" implements ").append(ifn).append(" {");
        b.append("public static final String defaultLocate = \"").append(conf.getDefault()).append("\";");
        for (LocateFile lang : files) {
            b.append("public static final ").append(lang.name).append(" ").append(lang.fieldName).append(" = new ")
                    .append(lang.name).append("();");
        }
        b.append("private static final HashMap<String, ").append(ifn).append("> map = new HashMap<>();");
        //
        b.append("static {");
        for (LocateFile lang : files) {
            b.append("map.put(\"").append(lang.locate).append("\", ").append(lang.fieldName).append(");");
        }
        b.append("}");
        b.append("public static ").append(ifn).append(" get(String locate) { return map.get(locate);}");
        b.append("private ").append(ifn).append(" impl;");
        b.append("public ").append(name).append("() { this.impl=").append(_default.fieldName).append(";}");
        b.append("public ").append(name).append("(String locate) { setLocate(locate); }");
        b.append("public void setLocate(String locate) { ").append(ifn).append(" impl = map.get(locate);")
                .append("this.impl = impl != null ? impl : ").append(_default.fieldName).append(";}");
        if (conf.supportServlet()) {
            b.append("private static class A {public final String l;public final float q;public A(String al){int i=al.indexOf(\";q=\");if(i==-1){l=al;q=1;}else{l=al.substring(0,i);q=Float.parseFloat(al.substring(i+3));}}}");
            b.append("public ").append(name).append("(HttpServletRequest req) { this.impl = get(req); }");
            b.append("public static ").append(ifn).append(" get(HttpServletRequest req){return get(req,defaultLocate);}");
            b.append("public static ").append(ifn)
                    .append(" get(HttpServletRequest req, String fallback) {ArrayList<A> b = new ArrayList<>();String c = req.getHeader(\"Accept-Language\");if (c != null && c.length() > 0) {String[] d = c.split(\",\");for (String e : d) {b.add(new A(e.trim()));}b.sort((e, f) -> -Float.compare(e.q, f.q));}for (A d : b) {")
                    .append(ifn)
                    .append(" e = map.get(d.l);if (e != null && d.q != 0) {return e;}}if (b.size() == 0) {return map.get(fallback);} else if (b.get(0).q > 0){return map.get(fallback);}else{ boolean e = false;for (A f : b) {if (fallback.equals(f.l)) {e = true;break;}}if (!e) { return map.get(fallback);}Set<Map.Entry<String, ")
                    .append(ifn).append(">> f = map.entrySet();for (A g : b){ for (Map.Entry<String, ").append(ifn)
                    .append("> h:f){if (!h.getKey().equals(g.l)){return h.getValue();}}}return map.get(fallback);}}");
        }
        for (AbstractMethod method : methods) {
            method.createJavaSource(b);
        }
        b.append("}");
        return b.toString();
    }

    @Override
    protected String createKotlinSource() {
        StringBuilder b = new StringBuilder();
        if (pkg.length() > 0) {
            b.append("package ").append(pkg).append("\n");
        }
        if (conf.supportServlet()) {
            b.append("import javax.servlet.http.HttpServletRequest;");
        }
        String ifn = conf.getInterface();
        b.append("class ").append(name).append(" : ").append(ifn).append(" {\nprivate var impl: ")
                .append(ifn).append("? = null\nconstructor() { impl = ").append(_default.fieldName)
                .append(" }\nconstructor(locate: String?) { setLocate(locate) }\nfun setLocate(locate: String?) {\nval impl = map[locate]\nthis.impl = impl ?: ")
                .append(_default.fieldName).append("\n}\n");
        if (conf.supportServlet()) {
            b.append("private class A(al: String) {\nvar l: String? = null\nvar q = 0f\ninit {\nval i = al.indexOf(\";q=\")\nif (i == -1) {\nl = al\nq = 1f\n} else {\nl = al.substring(0, i)\nq = al.substring(i + 3).toFloat()\n}}}\nconstructor(req: HttpServletRequest) { impl = get(req) }\n");
        }
        for (AbstractMethod method : methods) {
            method.createKotlinSource(b);
        }
        b.append("companion object {\nconst val defaultLocate = \"").append(_default.locate).append("\"\n");
        for (LocateFile lang : files) {
            b.append("val ").append(lang.fieldName).append(" = ").append(lang.name).append("()\n");
        }
        b.append("private val map = HashMap<String, ").append(ifn).append(">()\ninit {\n");
        for (LocateFile lang : files) {
            b.append("map[\"").append(lang.locate).append("\"] = ").append(lang.fieldName).append("\n");
        }
        b.append("}\noperator fun get(locate: String): ").append(ifn).append("?  = map[locate]\n");
        if (conf.supportServlet()) {
            b.append("operator fun get(req: HttpServletRequest): ")
                    .append(ifn)
                    .append("? = Companion[req, defaultLocate]\noperator fun get(req: HttpServletRequest, fallback: String): ")
                    .append(ifn)
                    .append("? {\nval b = ArrayList<A>()\nval c: String = req.getHeader(\"Accept-Language\")\nif (c.isNotEmpty()) {\nval d = c.split(\",\".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()\nfor (e in d) { b.add(A(e.trim { it <= ' ' })) }\nb.sortWith { e: A, f: A -> -e.q.compareTo(f.q) }\n}\nfor (d in b) {\nval e = map[d.l]\nif (e != null && d.q != 0f) {\nreturn e\n}\n}\nreturn if (b.size == 0) {\nmap[fallback]\n} else if (b[0].q > 0) {\nmap[fallback]\n} else {\nvar e = false\nfor (f in b) {\nif (fallback == f.l) {\ne = true\nbreak\n}\n}\nif (!e) {\nreturn map[fallback]\n}\nval f: Set<Map.Entry<String?, ").append(ifn).append(">> = map.entries\nfor (g in b) {\nfor ((key, value) in f) {\nif (key != g.l) {\nreturn value\n}\n}\n}\nmap[fallback]\n}\n}");
        }
        b.append("}}");
        return b.toString();
    }

}
