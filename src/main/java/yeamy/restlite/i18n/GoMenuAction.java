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

public class GoMenuAction extends AbstractMenuAction {

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
            VirtualFile dir = f.getParent();
            String mp = dir.getPath();
            for (String p : ps) {
                if (mp.startsWith(p)) {
                    createPackageFile(req, dir);
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
        if (!"go".equals(map.get("language"))) {
            return;
        }
        Configuration conf = new Configuration(map.get("package"), map.get("file"), map.get("default"), map.get("http"));
        //default
        VirtualFile defaultVf = todos.remove(conf.getDefault().toLowerCase() + ".lang");
        if (defaultVf == null) {
            throw new LangException("file \"" + conf.getDefault() + ".lang\" not exists");
        }
        ArrayList<LocateMethod> methods = new ArrayList<>();
        readFile(defaultVf, (fileName, line, key, text, from)
                -> methods.add(new LocateMethod(conf.getDefault(), key, fileName, line, text, from)));
        // output
        OutputFile file = new OutputFile(conf, methods);
        // locate
        for (VirtualFile f : todos.values()) {
            String locate = getLocate(f);
            readFile(f, (fileName, line, key, text, from) -> {
                OutputMethod method = file.getMethod(key);
                if (method == null) {
                    throw new LangException("Undefined method \"" + key + "\" in file " + f.getName());
                }
                file.add(new LocateMethod(locate, key, fileName, line, text, from));
            });
        }
        createSourceFile(req, dir, file);
    }

    protected void createSourceFile(Object req, VirtualFile dir, OutputFile f) throws LangException {
        String fileName = f.fileName();

        VirtualFile vf = dir.findChild(fileName);
        if (vf != null) {
            try {
                vf.delete(req);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new LangException("Fail to update file " + fileName);
            }
        }
        try {
            vf = dir.createChildData(req, fileName);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new LangException("Fail to create file " + fileName);
        }
        try (OutputStream os = vf.getOutputStream(req)) {
            os.write(f.createSource().getBytes());
            os.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new LangException("Fail to create file " + fileName);
        }
    }

    private static String getLocate(VirtualFile vf) {
        String name = vf.getName();
        return name.substring(0, name.lastIndexOf('.'));
    }

    protected static void readFile(VirtualFile vf, LineReader r) throws LangException {
        String fileName = vf.getName();
        try (InputStream is = vf.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            LineReader.readFile(fileName, reader, r);
        } catch (IOException e) {
            e.printStackTrace();
            throw new LangException("IO error while reading file " + fileName);
        }
    }

}
