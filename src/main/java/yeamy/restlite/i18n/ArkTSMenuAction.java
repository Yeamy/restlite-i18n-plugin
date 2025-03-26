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

public class ArkTSMenuAction extends AbstractMenuAction {

    @Override
    public void action(Object e, Project project) {
        try {
            findPackage(e, project);
        } catch (Exception ex) {
            showErrorDialog(ex.toString());
        }
    }

    private void findPackage(Object req, Project project) throws LangException {
        VirtualFile @NotNull [] src = ProjectRootManager.getInstance(project).getContentRoots();
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
                    createPackageFile(req, pkg);
                    break;
                }
            }
        }
    }

    protected void createPackageFile(Object req, VirtualFile dir) throws LangException {
        HashMap<String, VirtualFile> todos = new HashMap<>();
        VirtualFile[] fs = dir.getChildren();
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
        if (!"ArkTS".equals(language)) {
            return;
        }
        Configuration conf = new Configuration(map.get("name"), map.get("util"), map.get("default"));
        //default
        VirtualFile defaultVf = todos.remove(conf.getDefault().toLowerCase() + ".lang");
        if (defaultVf == null) {
            throw new LangException("Configuration file not exists");
        }
        ArrayList<LocateMethod> dms = new ArrayList<>();
        readFile(defaultVf, (fn, line, key, text, from)
                -> dms.add(new LocateMethod(key, fn, line, text, from)));
        LocateFile dlf = new LocateFile(conf, conf.getDefault(), dms);
        // interface
        ArrayList<InterfaceMethod> methods = new ArrayList<>(dms.size());
        dms.forEach(m -> methods.add(new InterfaceMethod(m)));
        InterfaceFile interfaceFile = new InterfaceFile(conf, methods);
        // util
        UtilFile utilFile = new UtilFile(conf, dlf);
        utilFile.addLocate(dlf);
        // locate
        for (VirtualFile f : todos.values()) {
            ArrayList<LocateMethod> lms = new ArrayList<>();
            readFile(f, (fn, line, key, text, from) -> {
                InterfaceMethod method = interfaceFile.get(key);
                if (method == null) {
                    throw new LangException("Undefined method \"" + key + "\" in file " + f.getName());
                }
                lms.add(new LocateMethod(method, fn, line, text, from));
            });
            String locate = getLocate(f);
            LocateFile lf = new LocateFile(conf, locate, lms);
            utilFile.addLocate(lf);
        }
        // write
        createClassFile(req, dir, interfaceFile);
        for (LocateFile f : utilFile.getLocates()) {
            createClassFile(req, dir, f);
        }
        createClassFile(req, dir, utilFile);
    }

    protected void createClassFile(Object req, VirtualFile dir, AbstractFile<?> f) throws LangException {
        String fileName = f.name + ".ets";

        VirtualFile vf = dir.findChild(fileName);
        if (vf != null) {
            try {
                vf.delete(req);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new LangException("Fail to update class " + f.name);
            }
        }
        try {
            vf = dir.createChildData(req, fileName);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new LangException("Fail to create class " + f.name);
        }
        try (OutputStream os = vf.getOutputStream(req)) {
            os.write(f.createSource().getBytes());
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

}
