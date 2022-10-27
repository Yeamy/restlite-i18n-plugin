package yeamy.restlite.i18n;

import com.intellij.ide.util.DirectoryChooser;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static yeamy.restlite.i18n.ActionImpl.*;

public abstract class AbstractMenuAction extends AnAction {

    @Override
    public final void actionPerformed(AnActionEvent e) {
        @Nullable Project project = e.getProject();
        if (project == null) return;
        @NotNull VirtualFile[] src = ProjectRootManager.getInstance(project).getContentSourceRoots();
        if (src.length == 0) {
            showDialog("Cannot get source Directories");
        } else if (src.length == 1) {
            action(e, src[0]);
        } else {
            PsiManager pm = PsiManager.getInstance(project);
            PsiDirectory[] ds = new PsiDirectory[src.length];
            for (int i = 0; i < src.length; i++) {
                ds[i] = pm.findDirectory(src[i]);
            }
            DirectoryChooser dc = new DirectoryChooser(project);
            dc.fillList(ds, ds[0], project, (String) null);
            dc.setTitle("Select Source Directory");
            dc.show();
            PsiDirectory sd = dc.getSelectedDirectory();
            if (dc.isOK() && sd != null) {
                action(e, sd.getVirtualFile());
            }
        }
    }

    protected void action(AnActionEvent e, VirtualFile srcVf) {
        try {
            VirtualFile p = srcVf.getParent();
            VirtualFile i18n = p.findChild(I18N_DIR);
            ApplicationManager.getApplication().runWriteAction(() -> {
                try {
                    if (i18n != null) action(e, srcVf, i18n);
                    else action(e, srcVf, p.createChildDirectory(e, I18N_DIR));
                } catch (Exception ex) {
                    showErrorDialog(ex.getMessage());
                    ex.printStackTrace();
                }
            });
        } catch (Exception ex) {
            showErrorDialog(ex.getMessage());
            ex.printStackTrace();
        }
    }

    protected abstract void action(AnActionEvent e, VirtualFile src, VirtualFile i18n) throws Exception;

}
