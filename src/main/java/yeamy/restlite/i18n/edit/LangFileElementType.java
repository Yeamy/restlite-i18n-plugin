package yeamy.restlite.i18n.edit;

import com.intellij.psi.tree.IFileElementType;

public class LangFileElementType extends IFileElementType {
    public static final LangFileElementType INSTANCE = new LangFileElementType();

    public LangFileElementType() {
        super(LangLanguage.INSTANCE);
    }
}
