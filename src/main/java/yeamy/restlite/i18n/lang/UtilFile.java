package yeamy.restlite.i18n.lang;

import java.util.ArrayList;
import java.util.Collections;

public class UtilFile extends AbstractFile<UtilMethod> {
    private final Configuration conf;
    private final ArrayList<LocateFile> locates;
    private final LocateFile defaultLocate;

    public UtilFile(Configuration conf, ArrayList<LocateFile> locates, LocateFile defaultLocate) {
        super(conf.getPackage(), conf.getUtil(), Collections.emptyList());
        this.conf = conf;
        this.locates = locates;
        this.defaultLocate = defaultLocate;
    }

    public void addLocate(LocateFile f) {
        locates.add(f);
    }

    public ArrayList<LocateFile> locates() {
        return locates;
    }

    @Override
    public String javaSource() {
        StringBuilder b = new StringBuilder();
        if (pkg.length() > 0) {
            b.append("package ").append(pkg).append(";\n\n");
        }
        if (conf.servlet == null) {
            b.append("import java.util.HashMap;\n");
        } else if (Configuration.SERVLET_JAVAX.equals(conf.servlet)) {
            b.append("""
                    import javax.servlet.http.HttpServletRequest;
                    import java.util.ArrayList;
                    import java.util.HashMap;
                    """);
        } else {
            b.append("""
                    import jakarta.servlet.http.HttpServletRequest;
                    import java.util.ArrayList;
                    import java.util.HashMap;
                    """);
        }
        b.append("\npublic final class ").append(name).append(" {\n");
        b.append("    public static final String DEFAULT_LOCATE = \"").append(conf.getDefault()).append("\";\n");
        for (LocateFile f : locates) {
            b.append("    public static final ").append(f.name).append(" ").append(f.fieldName).append(" = new ")
                    .append(f.name).append("();\n");
        }
        String ifn = conf.getInterface();
        b.append("    public static final ").append(ifn).append(" defaultLocate = ")
                .append(this.defaultLocate.fieldName).append(";\n");
        b.append("    private static final HashMap<String, ").append(ifn).append("> map = new HashMap<>();\n\n");
        b.append("    static {\n");
        for (LocateFile f : locates) {
            b.append("        map.put(\"").append(f.locate).append("\", ").append(f.fieldName).append(");\n");
        }
        b.append("""
                    }
                
                    public static ____ get(String locate) {
                        return map.get(locate);
                    }
                
                """.replace("____", ifn));
        if (conf.supportServlet()) {
            b.append("""
                        private static class A {
                            public final String l;
                            public final float q;
                    
                            public A(String al) {
                                int i = al.indexOf(";q=");
                                if (i == -1) {
                                    l = al;
                                    q = 1;
                                } else {
                                    l = al.substring(0, i);
                                    q = Float.parseFloat(al.substring(i + 3));
                                }
                            }
                        }
                    
                        public static ____ get(HttpServletRequest req) {
                            return get(req, defaultLocate);
                        }
                    
                        public static ____ get(HttpServletRequest req, ____ fallback) {
                            ArrayList<A> b = new ArrayList<>();
                            String c = req.getHeader("Accept-Language");
                            if (c != null && c.length() > 0) {
                                String[] d = c.split(",");
                                for (String e : d) {
                                    b.add(new A(e.trim()));
                                }
                                b.sort((e, f) -> -Float.compare(e.q, f.q));
                            }
                            for (A d : b) {
                                ____ e = map.get(d.l);
                                if (e != null && d.q != 0) {
                                    return e;
                                }
                            }
                            return fallback;
                        }
                    
                    """.replace("____", ifn));
        }
        for (UtilMethod method : methods) {
            method.createJavaSource(b);
        }
        b.append("}");
        return b.toString();
    }

    @Override
    public String kotlinSource() {
        StringBuilder b = new StringBuilder();
        if (pkg.length() > 0) {
            b.append("package ").append(pkg).append("\n\n");
        }
        if (conf.servlet != null) {
            if (Configuration.SERVLET_JAVAX.equals(conf.servlet)) {
                b.append("import javax.servlet.http.HttpServletRequest\n\n");
            } else {
                b.append("import jakarta.servlet.http.HttpServletRequest\n\n");
            }
        }
        b.append("final class ").append(name).append(" {\n");
        for (AbstractMethod method : methods) {
            method.createKotlinSource(b);
        }

        b.append("\n    companion object {\n        const val DEFAULT_LOCATE = \"").append(conf.getDefault()).append("\"\n");
        for (LocateFile lang : locates) {
            b.append("        @kotlin.jvm.JvmField\n        val ").append(lang.fieldName).append(" = ").
                    append(lang.name).append("()\n\n");
        }
        b.append("        @JvmStatic\n        val defaultLocate = ").append(this.defaultLocate.fieldName).append("\n\n");

        String ifn = conf.getInterface();
        b.append("        private val map = HashMap<String, ").append(ifn).append(">()\n        init {\n");
        for (LocateFile lang : locates) {
            b.append("            map[\"").append(lang.locate).append("\"] = ").append(lang.fieldName).append("\n");
        }
        b.append("""
                        }
                
                        @JvmStatic
                        operator fun get(locate: String): ____ = map[locate] ?: defaultLocate
                """.replace("____", ifn));
        if (conf.supportServlet()) {
            b.append("""
                    
                            @JvmStatic
                            operator fun get(req: HttpServletRequest): ____ = Companion[req, defaultLocate]
                    
                            @JvmStatic
                            operator fun get(req: HttpServletRequest, fallback: ____): ____ {
                                val b = ArrayList<A>()
                                val c: String = req.getHeader("Accept-Language")
                                if (c.isNotEmpty()) {
                                    val d = c.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                    for (e in d) {
                                        b.add(A(e.trim { it <= ' ' }))
                                    }
                                    b.sortWith { e: A, f: A -> -e.q.compareTo(f.q) }
                                }
                                for (d in b) {
                                    val e = map[d.l]
                                    if (e != null && d.q != 0f) {
                                        return e
                                    }
                                }
                                return fallback
                            }
                    
                             private class A(al: String) {
                                 var l: String? = null
                                 var q = 0f
                    
                                 init {
                                     val i = al.indexOf(";q=")
                                     if (i == -1) {
                                         l = al
                                         q = 1f
                                     } else {
                                         l = al.substring(0, i)
                                         q = al.substring(i + 3).toFloat()
                                     }
                                 }
                             }
                    """.replace("____", ifn));
        }
        b.append("    }\n}");
        return b.toString();
    }

}
