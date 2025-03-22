package yeamy.restlite.i18n;

import com.intellij.ide.util.DirectoryChooser;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;
import java.util.ArrayList;

public class TemplateMenuAction extends AbstractMenuAction {
    public static final String BUILD_LANG = """
            #RESTLite i18n configuration
            language=go
            
            #Name of package
            package=i18n
            
            #Name of source file
            file=i18n.go
            
            #Default language/locate(see more about http header Accept-Language: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Language)
            default=zh-CN
            
            #Set if generate auto-select-method with param "net/http"
            http=true
            """;

    public static final String CN_LANG = """
            #井号开头是备注
            #一行生成一个方法，等号左边为方法名，等号右边为文本内容（包括空格）
            #参数名用#{}标注，支持类型限制如下，不填类型既无限制
            Hello=你好#{name}，我是int #{int a},int8 #{i8 b},int16 #{i16 c},int32 #{i32 d},int64 #{i64 e},float64保留n位小数 #{f2 f},float64 #{f1 g}
            World=也可以是uint#{uint a},uint8 #{u8 b},int16 #{u16 c},int32 #{u32 d},int64 #{u64 e},bool #{bool f},string#{str s}, #{s2}
            #如果需要输出 #{ 请使用 ##{ 代替；\\前无需加转义符，除了\\n和\\r
            Txt=转义符示例##{name} " \\ \\b \\f \\t \\n \\r \\\\n\\\\r
            """;
    public static final String EN_LANG = """
            # I'm remark，start with '#'
            # One line generate one method, method name on the left of equals sign as the text content (include space) on the right
            # Param name in #{}, type limit supported, as the example below; none if no limit.
            Hello=Hello#{name},I'm int #{int a},int8 #{i8 b},int16 #{i16 c},int32 #{i32 d},int64 #{i64 e},float64 with n(here is 2) decimal places #{f2 f},float64 #{f1 g}
            World=Also can be string #{str s}, #{s2}, bool #{bool f}, uint#{uint a},uint8 #{u8 b},int16 #{u16 c},int32 #{u32 d},int64 #{u64 e}
            # Typing #{ with ##{ instead; no need to add escape character for \\ except \\n,\\r
            Txt=escape character sample_##{name} " \\ \\b \\f \\t \\n \\r \\\\n\\\\r
            """;

    @Override
    protected void action(Object e, Project project) {
        ApplicationManager.getApplication().invokeLater(() -> choosePackage(e, project));
    }

    private void addChildDir(ArrayList<PsiDirectory> list, @NotNull PsiDirectory dir) {
        for (PsiElement e : dir.getChildren()) {
            if (e instanceof PsiDirectory) {
                PsiDirectory d = (PsiDirectory) e;
                if (d.getName().charAt(0) != '.') {
                    list.add(d);
                    addChildDir(list, d);
                }
            }
        }
    }

    public void choosePackage(Object req, Project project) {
        VirtualFile @NotNull [] roots = ProjectRootManager.getInstance(project).getContentRoots();
        PsiDirectoryFactory factory = PsiDirectoryFactory.getInstance(project);
        DirectoryChooser dialog = new DirectoryChooser(project);
        ArrayList<PsiDirectory> list = new ArrayList<>();
        for (VirtualFile dir : roots) {
            PsiDirectory root = factory.createDirectory(dir);
            list.add(root);
            addChildDir(list, root);
            PsiDirectory[] dirs = new PsiDirectory[list.size()];
            dialog.fillList(list.toArray(dirs), root, project, (String) null);
        }

        if (dialog.showAndGet()) {
            PsiDirectory dir = dialog.getSelectedDirectory();
            ApplicationManager.getApplication().runWriteAction(() -> {
                try {
                    assert dir != null;
                    writeTemplateFile(req, dir.getVirtualFile());
                } catch (Exception ex) {
                    showErrorDialog(ex.toString());
                }
            });
        }
    }

    public static void writeTemplateFile(Object req, VirtualFile i18n) throws Exception {
        StringBuilder msg = new StringBuilder();
        VirtualFile config = i18n.findChild(BUILD_FILE);
        if (config == null || !config.exists()) {
            config = i18n.createChildData(req, BUILD_FILE);
            try (OutputStream os = config.getOutputStream(req)) {
                os.write(BUILD_LANG.getBytes());
            }
        } else {
            msg.append("Build file \"" + BUILD_FILE + "\" exists\n");
        }
        VirtualFile zhCN = i18n.findChild("zh-CN.lang");
        if (zhCN == null || !zhCN.exists()) {
            zhCN = i18n.createChildData(req, "zh-CN.lang");
            try (OutputStream os = zhCN.getOutputStream(req)) {
                os.write(CN_LANG.getBytes());
            }
        } else {
            msg.append("Template file \"/i18n/zh-CN.lang\" exists\n");
        }
        VirtualFile enUS = i18n.findChild("en-US.lang");
        if (enUS == null || !enUS.exists()) {
            enUS = i18n.createChildData(req, "en-US.lang");
            try (OutputStream os = enUS.getOutputStream(req)) {
                os.write(EN_LANG.getBytes());
            }
        } else {
            msg.append("Template file \"/i18n/en-US.lang\" exists\n");
        }
        if (msg.length() > 0) {
            showInfoDialog(msg.toString());
        }
    }

}
