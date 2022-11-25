package yeamy.restlite.i18n.edit;

import com.intellij.ide.highlighter.JavaHighlightingColors;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class LangSyntaxHighlighter extends SyntaxHighlighterBase {
//    com.intellij.ide.highlighter.JavaFileHighlighter

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FlexAdapter(new LangFlexLexer());
    }

    private static final TextAttributesKey[] STR_ESCAPE = new TextAttributesKey[]{
            JavaHighlightingColors.VALID_STRING_ESCAPE
    };
    private static final TextAttributesKey[] METHOD = new TextAttributesKey[]{
            JavaHighlightingColors.METHOD_DECLARATION_ATTRIBUTES
    };
    private static final TextAttributesKey[] SEPARATOR = new TextAttributesKey[]{
            JavaHighlightingColors.BRACKETS
    };
    private static final TextAttributesKey[] PARENTHESES = new TextAttributesKey[]{
            JavaHighlightingColors.PARENTHESES
    };
    private static final TextAttributesKey[] PARAM_TYPE = new TextAttributesKey[]{
            JavaHighlightingColors.TYPE_PARAMETER_NAME_ATTRIBUTES
    };
    private static final TextAttributesKey[] PARAM_NAME = new TextAttributesKey[]{
            JavaHighlightingColors.PARAMETER_ATTRIBUTES
    };
    private static final TextAttributesKey[] STRING = new TextAttributesKey[]{
            JavaHighlightingColors.STRING
    };
    private static final TextAttributesKey[] COMMENT = new TextAttributesKey[]{
            JavaHighlightingColors.LINE_COMMENT
    };

    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(LangTokenType.STR_ESCAPE)) {
            return STR_ESCAPE;
        }
        if (tokenType.equals(LangTokenType.METHOD)) {
            return METHOD;
        }
        if (tokenType.equals(LangTokenType.SEPARATOR)) {
            return SEPARATOR;
        }
        if (tokenType.equals(LangTokenType.PARAM_START) || tokenType.equals(LangTokenType.PARAM_END)) {
            return PARENTHESES;
        }
        if (tokenType.equals(LangTokenType.PARAM_TYPE)) {
            return PARAM_TYPE;
        }
        if (tokenType.equals(LangTokenType.PARAM_NAME)) {
            return PARAM_NAME;
        }
        if (tokenType.equals(LangTokenType.STRING)) {
            return STRING;
        }
        if (tokenType.equals(LangTokenType.COMMENT)) {
            return COMMENT;
        }
        //SPACE, ERROR
        return EMPTY_KEYS;
    }

}
