package yeamy.restlite.i18n;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.OutputStream;

public class TemplateMenuAction extends AbstractMenuAction {
    public static final String BUILD_LANG = """
            #RESTLite i18n configuration
            language=ArkTS
            
            #Name of source class
            name=I18n
            
            #Name of the util class
            util=I18nUtil
            
            #Default language/locate(see more about http header Accept-Language: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Language)
            default=zh-CN
            """;
    public static final String CN_LANG = """
            #井号开头是备注
            #一行生成一个方法，等号左边为方法名，等号右边为文本内容（包括空格）
            #参数名用#{}标注，支持类型限制如下，不填类型既无限制
            hello=你好#{name}，我是number #{num a}, boolean #{bool b}, Object #{obj o}, string #{str s}
            #如果需要输出 #{ 请使用 ##{ 代替；\\前无需加转义符，除了\\n和\\r
            txt=转义符示例##{name} " \\ \\b \\f \\t \\n \\r \\\\n\\\\r
            """;
    public static final String EN_LANG = """
            # I'm remark，start with '#'
            # One line generate one method, method name on the left of equals sign as the text content (include space) on the right
            # Param name in #{}, type limit supported, as the example below; none if no limit.
            hello=Hello #{name}, I'm string #{str s}, number #{num a}, boolean #{bool b}, Object #{obj o}
            # Typing #{ with ##{ instead; no need to add escape character for \\ except \\n,\\r
            txt=escape character sample_##{name} " \\ \\b \\f \\t \\n \\r \\\\n\\\\r
            """;

    @Override
    protected void action(Object e, Project project) {
        ApplicationManager.getApplication().invokeLater(() -> choosePackage(e, project));
    }

    public void choosePackage(Object req, Project project) {
        DirectoryChooserDialog dialog = new DirectoryChooserDialog("", project);
        if (dialog.showAndGet()) {
            VirtualFile dir = dialog.getSelectedDirectory();
            ApplicationManager.getApplication().runWriteAction(() -> {
                try {
                    writeTemplateFile(req, dir);
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
