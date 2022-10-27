package yeamy.restlite.i18n.lang;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        try {
            File i18n = new File("");// i18n dir
            createLang(i18n);
//            File src = new File("");// src dir
//            createJava(src, i18n);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final String CONFIGURATION_FILE = "configuration.lang";

    public static void createLang(File i18n) throws IOException {
        StringBuilder error = new StringBuilder();
        File config = new File(i18n, CONFIGURATION_FILE);
        if (!config.exists()) {
            try (OutputStream os = new FileOutputStream(config)) {
                os.write("#RestLite i18n configuration\n\n".getBytes());
                os.write("#Package name for i18n\n".getBytes());
                os.write("package=org.example.i18n\n\n".getBytes());
                os.write("#Class name for i18n\n".getBytes());
                os.write("name=I18n\n\n".getBytes());
                os.write("#Proxy class name for i18n\n".getBytes());
                os.write("proxy=I18nProxy\n\n".getBytes());
                os.write("#Set the default language/locate(see more about http header Accept-Language: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Language)\n".getBytes());
                os.write("default=zh-CN\n\n".getBytes());
                os.write("#if true generate auto-select-method with param HttpServletRequest (class in servlet)\n".getBytes());
                os.write("servlet=true\n".getBytes());
            }
        } else {
            error.append("Configuration file \"" + CONFIGURATION_FILE + "\" exists\n");
        }
        File zhCN = new File(i18n, "zh-CN.lang");
        if (!zhCN.exists()) {
            try (OutputStream os = new FileOutputStream(zhCN)) {
                os.write("#井号开头是备注\n".getBytes());
                os.write("#一行生成一个方法,等号左边问方法名，等号右边为文本内容（包括空格）\n".getBytes());
                os.write("#参数名用#{}标注，支持类型限制如下，不填类型既无限制\n".getBytes());
                os.write("hello=你好#{name}，\\n我是int#{int a}, long#{long l},char#{char c},float#{float f},double#{double d},string#{str s}\n".getBytes());
                os.write("#如果需要输出 #{ 请使用 ##{ 代替\n".getBytes());
                os.write("txt=纯文本##{name}\n".getBytes());
            }
        } else {
            error.append("Template file \"/i18n/zh-CN.lang\" exists\n");
        }
        File enUS = new File(i18n, "en-US.lang");
        if (!enUS.exists()) {
            try (OutputStream os = new FileOutputStream(enUS)) {
                os.write("# I'm remark，start with '#'\n".getBytes());
                os.write("# One line generate one method, method name on the left of '=' as the text content (include space) on the right\n".getBytes());
                os.write("# Param name in #{}, type limit supported, as the example below; none if no limit.\n".getBytes());
                os.write("hello=Hello#{name},\\nI'm string#{str s},int#{int a},long#{long l},char#{char c},float#{float f},double#{double d}\n".getBytes());
                os.write("# Typing #{ with ##{ instead\n".getBytes());
                os.write("txt=pure text ##{name}\n".getBytes());
            }
        } else {
            error.append("Template file \"/i18n/en-US.lang\" exists\n");
        }
        if (error.length() > 0) {
            System.out.println(error.toString());
        }
    }

    public static void createJava(File src, File i18n) throws LangException {
        HashMap<String, File> todos = new HashMap<>();
        File[] fs = i18n.listFiles();
        //config
        File config = null;
        if (fs != null) {
            for (File f : fs) {
                String name = f.getName();
                if (name.length() > 5) {
                    name = name.toLowerCase();
                    if (name.endsWith(".lang")) {
                        if (config == null && name.equals(CONFIGURATION_FILE)) {
                            config = f;
                        } else {
                            todos.put(name, f);
                        }
                    }
                }
            }
        }
        if (config == null || !config.exists()) {
            System.out.println("Configuration file not exists");
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        readFile(config, (fn, line, key, text, from) -> map.put(key, text.substring(from).trim()));
        Configuration conf = new Configuration(map.get("package"), map.get("name"), map.get("proxy"), map.get("default"),
                map.get("servlet"), map.get("restlite"));
        //default
        File defaultVf = todos.remove(conf.getDefault().toLowerCase() + ".lang");
        if (defaultVf == null) {
            System.out.println("Configuration file not exists");
            return;
        }
        ArrayList<LocateMethod> dms = new ArrayList<>();
        readFile(defaultVf, (fn, line, key, text, from)
                -> dms.add(new LocateMethod(key, fn, line, text, from)));
        LocateFile dlf = new LocateFile(conf, conf.getDefault(), dms);
        ArrayList<AbstractFile<?>> writeFiles = new ArrayList<>();
        writeFiles.add(dlf);
        // interface
        ArrayList<InterfaceMethod> ims = new ArrayList<>(dms.size());
        dms.forEach(m -> ims.add(InterfaceMethod.create(m)));
        InterfaceFile iff = new InterfaceFile(conf, ims);
        writeFiles.add(iff);
        // locate
        ArrayList<LocateFile> lfs = new ArrayList<>();
        lfs.add(dlf);
        for (File f : todos.values()) {
            ArrayList<LocateMethod> lms = new ArrayList<>();
            readFile(f, (fn, line, key, text, from) -> {
                InterfaceMethod ifm = iff.get(key);
                if (ifm == null) {
                    throw new LangException("Undefined method \"" + key + "\" in file " + f.getName());
                }
                lms.add(new LocateMethod(ifm, fn, line, text, from));
            });
            String locate = getLocate(f);
            LocateFile lf = new LocateFile(conf, locate, lms);
            lfs.add(lf);
            writeFiles.add(lf);
        }
        // proxy
        ArrayList<ProxyMethod> pms = new ArrayList<>();
        ims.forEach(m -> pms.add(new ProxyMethod(m)));
        writeFiles.add(new ProxyFile(conf, pms, lfs, dlf));
        File pkg = getPackage(src, conf.getPackage());
        // write
        for (AbstractFile<?> f : writeFiles) {
            createJavaFile(pkg, f);
        }
    }

    private static String getLocate(File f) {
        String name = f.getName();
        return name.substring(0, name.lastIndexOf('.'));
    }

    private static File getPackage(File src, String pkg) throws LangException {
        if (pkg.length() == 0) {
            return src;
        }
        String[] dirs = pkg.split("\\.");
        StringBuilder sb = new StringBuilder();
        sb.append(src);
        for (String dir : dirs) {
            sb.append(File.pathSeparatorChar).append(dir);
        }
        File f = new File(sb.toString());
        if (f.mkdirs()) {
            return f;
        }
        return null;
    }

    private static void createJavaFile(File pkg, AbstractFile<?> f) throws LangException {
        String fn = f.name + ".java";
        File vf = new File(pkg, fn);
        if (vf.exists()) {
            if (!vf.delete()) {
                throw new LangException("Fail to update class " + f.name);
            }
        }
        try (OutputStream os = new FileOutputStream(vf)) {
            f.createSourceFile(os);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new LangException("Fail to create class " + f.name);
        }
    }

    private static void readFile(File f, LineReader r) throws LangException {
        String fn = f.getName();
        try (InputStream is = new FileInputStream(f);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            LineReader.readFile(fn, reader, r);
        } catch (IOException e) {
            e.printStackTrace();
            throw new LangException("IO error while reading file " + fn);
        }
    }
}
