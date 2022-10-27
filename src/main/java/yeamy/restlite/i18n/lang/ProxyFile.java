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
    protected String createSourceCode() {
        StringBuilder b = new StringBuilder("package ").append(pkg).append(";");
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
            method.createSourceCode(b);
        }
        b.append("}");
        return b.toString();
    }

}
