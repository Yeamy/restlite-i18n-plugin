package yeamy.restlite.i18n.edit;

import com.intellij.psi.JavaTokenType;
import com.intellij.psi.StringEscapesTokenTypes;
import com.intellij.psi.TokenType;
import com.intellij.psi.impl.source.tree.JavaElementType;
import com.intellij.psi.tree.IElementType;

public interface LangTokenType {
    IElementType SPACE = TokenType.WHITE_SPACE;
    IElementType ERROR = TokenType.BAD_CHARACTER;
    IElementType METHOD = JavaElementType.METHOD;
    IElementType SEPARATOR = JavaTokenType.EQ;
    IElementType PARAM_START = JavaTokenType.LBRACE;
    IElementType PARAM_END = JavaTokenType.RBRACE;
    IElementType PARAM_TYPE = JavaElementType.TYPE;
    IElementType PARAM_NAME = JavaElementType.PARAMETER;
    IElementType STRING = JavaTokenType.STRING_LITERAL;
    IElementType STR_ESCAPE = StringEscapesTokenTypes.VALID_STRING_ESCAPE_TOKEN;
    IElementType COMMENT = JavaTokenType.END_OF_LINE_COMMENT;
}
