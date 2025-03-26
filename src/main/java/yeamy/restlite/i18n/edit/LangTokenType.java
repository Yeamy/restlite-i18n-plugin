package yeamy.restlite.i18n.edit;

import com.intellij.psi.StringEscapesTokenTypes;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

public interface LangTokenType {
    IElementType SPACE = TokenType.WHITE_SPACE;
    IElementType ERROR = TokenType.BAD_CHARACTER;
    IElementType METHOD = new IElementType("METHOD", LangLanguage.INSTANCE);
    IElementType SEPARATOR = new IElementType("SEPARATOR", LangLanguage.INSTANCE);
    IElementType PARAM_START = new IElementType("PARAM_START", LangLanguage.INSTANCE);
    IElementType PARAM_END = new IElementType("PARAM_END", LangLanguage.INSTANCE);
    IElementType PARAM_TYPE = new IElementType("PARAM_TYPE", LangLanguage.INSTANCE);
    IElementType PARAM_NAME = new IElementType("PARAM_NAME", LangLanguage.INSTANCE);
    IElementType STRING = new IElementType("STRING", LangLanguage.INSTANCE);
    IElementType STR_ESCAPE = StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN;
    IElementType COMMENT = new IElementType("COMMENT", LangLanguage.INSTANCE);
}
