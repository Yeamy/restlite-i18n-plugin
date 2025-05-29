package yeamy.restlite.i18n.edit;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class LangTokenType extends IElementType {
    public static final IElementType CRLF = new LangTokenType("CRLF");
    public static final IElementType ERROR = new LangTokenType("ERROR");
    public static final IElementType METHOD = new LangTokenType("METHOD");
    public static final IElementType METHOD_NAME = new LangTokenType("METHOD_NAME");
    public static final IElementType SEPARATOR = new LangTokenType("SEPARATOR");
    public static final IElementType PARAM_START = new LangTokenType("PARAM_START");
    public static final IElementType PARAM_END = new LangTokenType("PARAM_END");
    public static final IElementType PARAM_TYPE = new LangTokenType("PARAM_TYPE");
    public static final IElementType PARAM_NAME = new LangTokenType("PARAM_NAME");
    public static final IElementType STRING = new LangTokenType("STRING");
    public static final IElementType STR_ESCAPE = new LangTokenType("STR_ESCAPE");
    public static final IElementType COMMENT = new LangTokenType("COMMENT");

    public LangTokenType(@NonNls @NotNull String debugName) {
        super(debugName, LangLanguage.INSTANCE);
    }

}
