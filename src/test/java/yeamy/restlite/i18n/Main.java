package yeamy.restlite.i18n;

import yeamy.restlite.i18n.lang.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        try {
            File i18n = new File("");// i18n dir
            createLang(i18n);
            String pkg = "";// package name
            File src = new File("");// src dir
            createJava(pkg, src, i18n);
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
                os.write(LangMenuAction.BUILD_LANG.getBytes());
            }
        } else {
            error.append("Configuration file \"" + CONFIGURATION_FILE + "\" exists\n");
        }
        File zhCN = new File(i18n, "zh-CN.lang");
        if (!zhCN.exists()) {
            try (OutputStream os = new FileOutputStream(zhCN)) {
                os.write(LangMenuAction.CN_LANG.getBytes());
            }
        } else {
            error.append("Template file \"/i18n/zh-CN.lang\" exists\n");
        }
        File enUS = new File(i18n, "en-US.lang");
        if (!enUS.exists()) {
            try (OutputStream os = new FileOutputStream(enUS)) {
                os.write(LangMenuAction.EN_LANG.getBytes());
            }
        } else {
            error.append("Template file \"/i18n/en-US.lang\" exists\n");
        }
        if (error.length() > 0) {
            System.out.println(error);
        }
    }

    public static void createJava(String pkg, File src, File i18n) throws LangException {
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
        Configuration conf = new Configuration(pkg, map.get("name"), map.get("proxy"), map.get("default"),
                map.get("servlet"));
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
        // write
        for (AbstractFile<?> f : writeFiles) {
            createJavaFile(src, f);
        }
    }

    private static String getLocate(File f) {
        String name = f.getName();
        return name.substring(0, name.lastIndexOf('.'));
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
            f.createJavaSource(os);
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
