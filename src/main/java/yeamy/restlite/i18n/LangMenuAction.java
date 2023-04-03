package yeamy.restlite.i18n;

import com.intellij.execution.ExecutionBundle;
import com.intellij.ide.util.DirectoryChooser;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

public class LangMenuAction extends AbstractMenuAction {
    public static final String BUILD_LANG = """
            #RESTLite i18n configuration
            
            #Name of real subject interface
            name=I18n
            
            #Name of the proxy class
            proxy=I18nProxy
            
            #Default language/locate(see more about http header Accept-Language: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Language)
            default=zh-CN
            
            #Set if generate auto-select-method with param HttpServletRequest (class in servlet)
            #it may be one of(since version 2.0): none/jakarta/javax
            servlet=javax
            """;
    public static final String CN_LANG = """
            #井号开头是备注
            #一行生成一个方法，等号左边问方法名，等号右边为文本内容（包括空格）
            #参数名用#{}标注，支持类型限制如下，不填类型既无限制
            hello=你好#{name}，我是int#{int a}, long#{long l},short#{short b},char#{char c},float#{float f},double#{double d},string#{str s}
            #如果需要输出 #{ 请使用 ##{ 代替；\\前无需加转义符，除了\\n和\\r
            txt=转义符示例##{name} " \\ \\b \\f \\t \\n \\r \\\\n\\\\r
            """;
    public static final String EN_LANG = """
            # I'm remark，start with '#'
            # One line generate one method, method name on the left of equals sign as the text content (include space) on the right
            # Param name in #{}, type limit supported, as the example below; none if no limit.
            hello=Hello#{name},I'm string#{str s},int#{int a},long#{long l},short#{short b},char#{char c},float#{float f},double#{double d}
            # Typing #{ with ##{ instead; no need to add escape character for \\ except \\n,\\r
            txt=escape character sample_##{name} " \\ \\b \\f \\t \\n \\r \\\\n\\\\r
            """;

    @Override
    public void action(Object req, Project project) {
        PackageChooserDialog dialog = new PackageChooserDialog(ExecutionBundle.message("choose.package.dialog.title"), project);
        dialog.show();
        PsiPackage pkg = dialog.getSelectedPackage();
        if (pkg == null) {
            return;
        }
        PsiDirectory @NotNull [] ds = pkg.getDirectories();
        PsiDirectory select;
        if (ds.length == 0) {
            return;
        } else if (ds.length == 1) {
            select = ds[0];
        } else {
            DirectoryChooser chooser = new DirectoryChooser(project);
            chooser.setTitle("More Than One Directory !");
            chooser.fillList(ds, ds[0], project, "");
            chooser.show();
            select = chooser.getSelectedDirectory();
            if (select == null) return;
        }
        ApplicationManager.getApplication().runWriteAction(() -> {
            try {
                createLang(req, select.getVirtualFile());
            } catch (Exception ex) {
                showErrorDialog(ex.toString());
            }
        });
    }

    public static void createLang(Object req, VirtualFile i18n) throws Exception {
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
            showDialog(msg.toString());
        }
    }

}
