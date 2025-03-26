package yeamy.restlite.i18n;

import com.intellij.execution.ExecutionBundle;
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

public abstract class AbstractClassMenuAction extends AbstractMenuAction {

    @Override
    public void action(Object e, Project project) {
        try {
            findPackage(e, project);
        } catch (Exception ex) {
            showErrorDialog(ex.toString());
        }
    }

    private void findPackage(Object req, Project project) throws LangException {
        VirtualFile @NotNull [] src = ProjectRootManager.getInstance(project).getContentSourceRoots();
        String[] ps = new String[src.length];
        for (int i = 0; i < src.length; i++) {
            ps[i] = src[i].getPath();
        }
        @NotNull Collection<VirtualFile> fs = FilenameIndex.getVirtualFilesByName(BUILD_FILE,
                GlobalSearchScope.projectScope(project));
        if (fs.isEmpty()) {
            throw new LangException(ExecutionBundle.message("script.exception.file.not.found", "build.lang"));
        }
        for (VirtualFile f : fs) {
            VirtualFile pkg = f.getParent();
            String mp = pkg.getPath();
            for (String p : ps) {
                if (mp.startsWith(p)) {
                    String pkgName = mp.length() > p.length()
                            ? mp.substring(p.length() + 1).replace("/", ".")
                            : "";
                    createPackageFile(req, pkg, pkgName);
                    break;
                }
            }
        }
    }

    protected void createPackageFile(Object req, VirtualFile pkg, String pkgName) throws LangException {
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
        if (todos.isEmpty()) {
            throw new LangException("No any source \"*.lang\" file found!");
        }
        HashMap<String, String> map = new HashMap<>();
        assert build != null;
        readFile(build, (fn, line, key, text, from) -> map.put(key, text.substring(from).trim()));
        String language = map.get("language");
        if (language != null && !language.equals("java")) {
            return;
        }
        Configuration conf = new Configuration(pkgName, map.get("name"), map.get("util"), map.get("proxy"),
                map.get("default"), map.get("servlet"));
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
                lms.add(new LocateMethod(ifm.name, ifm.params, fn, line, text, from));
            });
            String locate = getLocate(f);
            LocateFile lf = new LocateFile(conf, locate, lms);
            lfs.add(lf);
            writeFiles.add(lf);
        }
        // util
        if (conf.hasUtil()) {
            writeFiles.add(new UtilFile(conf, lfs, dlf));
        }
        // proxy
        if (conf.hasProxy()) {
            ArrayList<ProxyMethod> pms = new ArrayList<>();
            ims.forEach(m -> pms.add(new ProxyMethod(m)));
            writeFiles.add(new ProxyFile(conf, pms, lfs, dlf));
        }
        // write
        for (AbstractFile<?> f : writeFiles) {
            createClassFile(req, pkg, f);
        }
    }

    protected void createClassFile(Object req, VirtualFile pkg, AbstractFile<?> f) throws LangException {
        String fileName = f.name + fileExtension();

        VirtualFile vf = pkg.findChild(fileName);
        if (vf != null) {
            try {
                vf.delete(req);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new LangException("Fail to update class " + f.name);
            }
        }
        try {
            vf = pkg.createChildData(req, fileName);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new LangException("Fail to create class " + f.name);
        }
        try (OutputStream os = vf.getOutputStream(req)) {
            os.write(getFileData(f));
            os.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new LangException("Fail to create class " + f.name);
        }
    }

    private static String getLocate(VirtualFile vf) {
        String name = vf.getName();
        return name.substring(0, name.lastIndexOf('.'));
    }

    protected static void readFile(VirtualFile vf, LineReader r) throws LangException {
        String fn = vf.getName();
        try (InputStream is = vf.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            LineReader.readFile(fn, reader, r);
        } catch (IOException e) {
            e.printStackTrace();
            throw new LangException("IO error while reading file " + fn);
        }
    }

    protected abstract String fileExtension();

    protected abstract byte[] getFileData(AbstractFile<?> f);

}
