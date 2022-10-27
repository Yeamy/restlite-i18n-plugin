package yeamy.restlite.i18n;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;

public class LangMenuAction extends AbstractMenuAction {

    @Override
    protected void action(AnActionEvent e, VirtualFile src, VirtualFile i18n) throws Exception {
        ActionImpl.createLang(e, i18n);
    }

}
