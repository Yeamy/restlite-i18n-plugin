package yeamy.restlite.i18n;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import yeamy.restlite.i18n.lang.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class JavaMenuAction extends AbstractMenuAction {

    @Override
    protected void action(AnActionEvent e, VirtualFile src, VirtualFile i18n) throws LangException {
        ActionImpl.createJava(e, src, i18n);
    }

}
