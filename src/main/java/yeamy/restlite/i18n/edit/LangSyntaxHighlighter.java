package yeamy.restlite.i18n.edit;

import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class LangSyntaxHighlighter extends SyntaxHighlighterBase {

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FlexAdapter(new LangFlexLexer());
    }

    private static final TextAttributesKey[] STR_ESCAPE = new TextAttributesKey[]{
            DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE
    };
    private static final TextAttributesKey[] METHOD_NAME = new TextAttributesKey[]{
            DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
    };
    private static final TextAttributesKey[] SEPARATOR = new TextAttributesKey[]{
            DefaultLanguageHighlighterColors.BRACKETS
    };
    private static final TextAttributesKey[] PARENTHESES = new TextAttributesKey[]{
            DefaultLanguageHighlighterColors.PARENTHESES
    };
    private static final TextAttributesKey[] PARAM_TYPE = new TextAttributesKey[]{
            DefaultLanguageHighlighterColors.PARAMETER
    };
    private static final TextAttributesKey[] PARAM_NAME = new TextAttributesKey[]{
            DefaultLanguageHighlighterColors.INSTANCE_FIELD
    };
    private static final TextAttributesKey[] STRING = new TextAttributesKey[]{
            DefaultLanguageHighlighterColors.STRING
    };
    private static final TextAttributesKey[] COMMENT = new TextAttributesKey[]{
            DefaultLanguageHighlighterColors.LINE_COMMENT
    };
    private static final TextAttributesKey[] ERROR = new TextAttributesKey[]{
            HighlightInfoType.ERROR.getAttributesKey()
    };

    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(LangTokenType.STR_ESCAPE)) {
            return STR_ESCAPE;
        } else if (tokenType.equals(LangTokenType.METHOD_NAME)) {
            return METHOD_NAME;
        } else if (tokenType.equals(LangTokenType.SEPARATOR)) {
            return SEPARATOR;
        } else if (tokenType.equals(LangTokenType.PARAM_START) || tokenType.equals(LangTokenType.PARAM_END)) {
            return PARENTHESES;
        } else if (tokenType.equals(LangTokenType.PARAM_TYPE)) {
            return PARAM_TYPE;
        } else if (tokenType.equals(LangTokenType.PARAM_NAME)) {
            return PARAM_NAME;
        } else if (tokenType.equals(LangTokenType.STRING)) {
            return STRING;
        } else if (tokenType.equals(LangTokenType.COMMENT)) {
            return COMMENT;
        } else if (tokenType.equals(LangTokenType.ERROR)) {
            return ERROR;
        }
        //SPACE
        return EMPTY_KEYS;
    }

}
