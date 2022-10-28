package yeamy.restlite.i18n;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import yeamy.restlite.i18n.lang.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ActionImpl {
    public static final String I18N_DIR = "restlite_i18n";
    public static final String CONFIGURATION_FILE = "configuration.lang";

    public static void createLang(AnActionEvent e, VirtualFile i18n) throws Exception {
        StringBuilder error = new StringBuilder();
        VirtualFile config = i18n.findChild(CONFIGURATION_FILE);
        if (config == null || !config.exists()) {
            config = i18n.createChildData(e, CONFIGURATION_FILE);
            try (OutputStream os = config.getOutputStream(e)) {
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
        VirtualFile zhCN = i18n.findChild("zh-CN.lang");
        if (zhCN == null || !zhCN.exists()) {
            zhCN = i18n.createChildData(e, "zh-CN.lang");
            try (OutputStream os = zhCN.getOutputStream(e)) {
                os.write("#井号开头是备注\n".getBytes());
                os.write("#一行生成一个方法,等号左边问方法名，等号右边为文本内容（包括空格）\n".getBytes());
                os.write("#参数名用#{}标注，支持类型限制如下，不填类型既无限制\n".getBytes());
                os.write("hello=你好#{name}，我是int#{int a}, long#{long l},char#{char c},float#{float f},double#{double d},string#{str s}\n".getBytes());
                os.write("#如果需要输出 #{ 请使用 ##{ 代替；\"无需加转义符；\\也无需加转义符，除非它后面紧跟着b、t、n、f、r和\\ \n".getBytes());
                os.write("txt=纯文本##{name} \" \\ \\\\t \\n \\\\\n".getBytes());
            }
        } else {
            error.append("Template file \"/i18n/zh-CN.lang\" exists\n");
        }
        VirtualFile enUS = i18n.findChild("en-US.lang");
        if (enUS == null || !enUS.exists()) {
            enUS = i18n.createChildData(e, "en-US.lang");
            try (OutputStream os = enUS.getOutputStream(e)) {
                os.write("# I'm remark，start with '#'\n".getBytes());
                os.write("# One line generate one method, method name on the left of '=' as the text content (include space) on the right\n".getBytes());
                os.write("# Param name in #{}, type limit supported, as the example below; none if no limit.\n".getBytes());
                os.write("hello=Hello#{name},I'm string#{str s},int#{int a},long#{long l},char#{char c},float#{float f},double#{double d}\n".getBytes());
                os.write("# Typing #{ with ##{ instead; no need to add escape character for \" nor \\ unless \\b,\\t,\\n,\\f,\\r,\\\\ appear \n".getBytes());
                os.write("txt=pure text ##{name} \" \\ \\\\t \\n \\\\\n".getBytes());
            }
        } else {
            error.append("Template file \"/i18n/en-US.lang\" exists\n");
        }
        if (error.length() > 0) {
            showDialog(error.toString());
        }
    }

    public static void createJava(AnActionEvent e, VirtualFile src, VirtualFile i18n) throws LangException {
        HashMap<String, VirtualFile> todos = new HashMap<>();
        VirtualFile[] fs = i18n.getChildren();
        //config
        VirtualFile config = null;
        for (VirtualFile f : fs) {
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
        if (config == null || !config.exists()) {
            showErrorDialog("Configuration file not exists");
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        readFile(config, (fn, line, key, text, from) -> map.put(key, text.substring(from).trim()));
        Configuration conf = new Configuration(map.get("package"), map.get("name"), map.get("proxy"), map.get("default"),
                map.get("servlet"), map.get("restlite"));
        //default
        VirtualFile defaultVf = todos.remove(conf.getDefault().toLowerCase() + ".lang");
        if (defaultVf == null) {
            showErrorDialog("Configuration file not exists");
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
        for (VirtualFile f : todos.values()) {
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
        VirtualFile pkg = getPackage(e, src, conf.getPackage());
        // write
        for (AbstractFile<?> f : writeFiles) {
            createJavaFile(e, pkg, f);
        }
    }

    private static void readFile(VirtualFile vf, LineReader r) throws LangException {
        String fn = vf.getName();
        try (InputStream is = vf.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            LineReader.readFile(fn, reader, r);
        } catch (IOException e) {
            e.printStackTrace();
            throw new LangException("IO error while reading file " + fn);
        }
    }

    private static String getLocate(VirtualFile vf) {
        String name = vf.getName();
        return name.substring(0, name.lastIndexOf('.'));
    }

    private static VirtualFile getPackage(AnActionEvent e, VirtualFile src, String pkg) throws LangException {
        if (pkg.length() == 0) {
            return src;
        }
        String[] dirs = pkg.split("\\.");
        VirtualFile vf = src;
        StringBuilder sb = new StringBuilder();
        for (String dir : dirs) {
            VirtualFile f = vf.findChild(dir);
            if (f == null) {
                try {
                    vf = vf.createChildDirectory(e, dir);
                    sb.append(src).append('.');
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw new LangException("Fail to create package " + sb);
                }
            } else {
                vf = f;
            }
        }
        return vf;
    }


    private static void createJavaFile(AnActionEvent e, VirtualFile pkg, AbstractFile<?> f) throws LangException {
        String fn = f.name + ".java";
        VirtualFile vf = pkg.findChild(fn);
        if (vf != null) {
            try {
                vf.delete(e);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new LangException("Fail to update class " + f.name);
            }
        }
        try {
            vf = pkg.createChildData(e, fn);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new LangException("Fail to create class " + f.name);
        }
        try (OutputStream os = vf.getOutputStream(e)) {
            f.createSourceFile(os);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new LangException("Fail to create class " + f.name);
        }
    }

    public static void showDialog(String message) {
        ApplicationManager.getApplication().invokeLater(() -> Messages.showInfoMessage(message, "RestLite I18n"));
    }

    public static void showErrorDialog(String message) {
        ApplicationManager.getApplication().invokeLater(() -> Messages.showInfoMessage(message, "RestLite I18n Error"));
    }
}
