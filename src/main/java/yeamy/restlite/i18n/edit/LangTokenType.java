package yeamy.restlite.i18n.edit;

import com.intellij.lang.Language;
import com.intellij.psi.StringEscapesTokenTypes;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

public interface LangTokenType {
    IElementType SPACE = TokenType.WHITE_SPACE;
    IElementType ERROR = TokenType.BAD_CHARACTER;
    IElementType METHOD = new IElementType("METHOD", Language.ANY);
    IElementType SEPARATOR = new IElementType("SEPARATOR", Language.ANY);
    IElementType PARAM_START = new IElementType("PARAM_START", Language.ANY);
    IElementType PARAM_END = new IElementType("PARAM_END", Language.ANY);
    IElementType PARAM_TYPE = new IElementType("PARAM_TYPE", Language.ANY);
    IElementType PARAM_NAME = new IElementType("PARAM_NAME", Language.ANY);
    IElementType STRING = new IElementType("STRING", Language.ANY);
    IElementType STR_ESCAPE = StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN;
    IElementType COMMENT = new IElementType("COMMENT", Language.ANY);
}
