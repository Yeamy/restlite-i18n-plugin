package yeamy.restlite.i18n;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMenuAction extends AnAction {
    public static final String BUILD_FILE = "build.lang";

    @Override
    public final void actionPerformed(AnActionEvent e) {
        @Nullable Project project = e.getProject();
        if (project == null) {
            showErrorDialog("Cannot get project");
            return;
        }
        ApplicationManager.getApplication().runWriteAction(() -> action(e, project));
    }

    protected abstract void action(Object req, Project project);

    public static void showInfoDialog(String message) {
        ApplicationManager.getApplication().invokeLater(() -> Messages.showInfoMessage(message, "RESTLite i18n"));
    }

    public static void showErrorDialog(String message) {
        ApplicationManager.getApplication().invokeLater(() -> Messages.showErrorDialog(message, "RESTLite i18n Error"));
    }
}
