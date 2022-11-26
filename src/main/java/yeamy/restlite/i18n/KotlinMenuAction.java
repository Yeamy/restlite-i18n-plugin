package yeamy.restlite.i18n;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;
import yeamy.restlite.i18n.lang.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class KotlinMenuAction extends AbstractMenuAction {

    @Override
    public void action(Object req, Project project) {
        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                createKotlin(req, project);
            } catch (Exception ex) {
                showErrorDialog(ex.toString());
            }
        });
    }

    public static void createKotlin(Object req, Project project) throws Exception {
        VirtualFile @NotNull [] src = ProjectRootManager.getInstance(project).getContentSourceRoots();
        String[] ps = new String[src.length];
        for (int i = 0; i < src.length; i++) {
            ps[i] = src[i].getPath();
        }
        @NotNull Collection<VirtualFile> fs = FilenameIndex.getVirtualFilesByName(BUILD_FILE,
                GlobalSearchScope.projectScope(project));
        for (VirtualFile f : fs) {
            VirtualFile pkg = f.getParent();
            String mp = pkg.getPath();
            for (String p : ps) {
                if (mp.startsWith(p)) {
                    String pkgName = mp.length() > p.length()
                            ? mp.substring(p.length() + 1).replace("/", ".")
                            : "";
                    createKotlin(req, pkg, pkgName);
                    break;
                }
            }
        }
    }

    public static void createKotlin(Object req, VirtualFile pkg, String pkgName) throws Exception {
        HashMap<String, VirtualFile> todos = new HashMap<>();
        VirtualFile[] fs = pkg.getChildren();
        //config
        VirtualFile build = null;
        for (VirtualFile f : fs) {
            String name = f.getName();
            if (name.length() > 5) {
                name = name.toLowerCase();
                if (name.endsWith(".lang")) {
                    if (build == null && name.equals(BUILD_FILE)) {
                        build = f;
                    } else {
                        todos.put(name, f);
                    }
                }
            }
        }
        if (build == null || !build.exists()) {
            throw new LangException("file build.lang not exists");
        }
        HashMap<String, String> map = new HashMap<>();
        readFile(build, (fn, line, key, text, from) -> map.put(key, text.substring(from).trim()));
        Configuration conf = new Configuration(pkgName, map.get("name"), map.get("proxy"), map.get("default"),
                map.get("servlet"), map.get("restlite"));
        //default
        VirtualFile defaultVf = todos.remove(conf.getDefault().toLowerCase() + ".lang");
        if (defaultVf == null) {
            throw new LangException("Configuration file not exists");
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
//        VirtualFile pkg = getPackage(req, src, conf.getPackage());
        // write
        for (AbstractFile<?> f : writeFiles) {
            createKotlinFile(req, pkg, f);
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

    private static void createKotlinFile(Object req, VirtualFile pkg, AbstractFile<?> f) throws LangException {
        String fn = f.name + ".kt";
        VirtualFile vf = pkg.findChild(fn);
        if (vf != null) {
            try {
                vf.delete(req);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new LangException("Fail to update class " + f.name);
            }
        }
        try {
            vf = pkg.createChildData(req, fn);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new LangException("Fail to create class " + f.name);
        }
        try (OutputStream os = vf.getOutputStream(req)) {
            f.createKotlinSource(os);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new LangException("Fail to create class " + f.name);
        }
    }

}
