package yeamy.restlite.i18n.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public class OutputFile {
    private final Configuration conf;
    private final LinkedHashMap<String, OutputMethod> methods = new LinkedHashMap<>();
    private LinkedHashSet<String> locate = new LinkedHashSet<>();

    public OutputFile(Configuration conf, ArrayList<LocateMethod> methods) {
        this.conf = conf;
        methods.forEach(m -> this.methods.put(m.name, new OutputMethod(m)));
        this.locate.add(conf.getDefault());
    }

    public OutputMethod getMethod(String name) {
        return methods.get(name);
    }

    public String createSource() {
        StringBuilder b = new StringBuilder();
        if (conf.getPackage().length() > 0) {
            b.append("package ").append(conf.getPackage()).append('\n');
        }
        int importIndex = b.length();
        if (conf.supportHttp()) {
            String module = """
                    import (
                      "net/http"
                      "strconv"
                      "strings"
                    )
                    
                    func GetLanguage(r *http.Request) string {
                        arr := r.Header.Values("Accept-Language")
                        if arr == nil || len(arr) == 0 {
                            return %s
                        } else if strings.EqualFold(arr[0], "*") {
                            return %s
                        }
                        selectLang := %s
                        selectQ := 0.1
                        for _, v := range arr {
                            substr := strings.Split(v, ";")
                            lang := strings.Split(substr[0], ",")[0]
                            if lang != selectLang && len(substr) > 1 && len(substr[1]) > 2 {
                                strQ := substr[1]
                                start := strings.LastIndex(strQ, "=") + 1
                                end := len(strQ)
                                q, err := strconv.ParseFloat(strQ[start:end], 64)
                                if err == nil && q >= selectQ {
                                    for _, value := range []string{%s} {
                                        if value == lang {
                                            selectLang = lang
                                            break
                                        }
                                    }
                                }
                            }
                        }
                        return selectLang
                    }
                    """;
            String defaultLang = '"' + conf.getDefault() + '"';
            b.append(String.format(module, defaultLang, defaultLang, defaultLang, locateArray()));
        } else {
            TreeSet<String> imports = new TreeSet<>();
            methods.values().forEach(m -> imports.addAll(m.imports()));
            if (imports.size() > 0) {
                StringBuilder importStr = new StringBuilder();
                importStr.append("import (");
                imports.forEach(pkg -> importStr.append("    \"").append(pkg).append("\"\n"));
                importStr.append(")");
                b.insert(importIndex, importStr);
            }
        }
        methods.values().forEach(m -> m.createSource(b));
        return b.toString();
    }

    private String locateArray() {
        StringBuilder sb = new StringBuilder();
        locate.forEach(k -> sb.append('"').append(k).append('"').append(", "));
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    public void add(LocateMethod m) {
        locate.add(m.locate);
        methods.get(m.name).add(m);
    }

    public String fileName() {
        return conf.getFileName();
    }
}
