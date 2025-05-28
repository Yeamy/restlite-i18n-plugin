package yeamy.restlite.i18n.edit;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

public class LangPsiFile extends PsiFileBase {

    public LangPsiFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, LangLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return LangFileType.INSTANCE;
    }
}
