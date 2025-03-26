package yeamy.restlite.i18n.edit;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

public class LangFlexLexer implements FlexLexer {
    private final static int START = 0;
    private final static int SEPARATOR = 1;
    private final static int STRING = 2;
    private final static int STR_ESCAPE = 3;
    private final static int PARAM_START = 4;
    private final static int PARAM_END = 5;
    private final static int PARAM_TYPE = 6;
    private final static int PARAM_NAME = 7;

    private int end, state, tokenStart, tokenEnd;
    private CharSequence buf;

    /**
     * Enters a new lexical state
     *
     * @param state the new lexical state
     */
    @Override
    public void yybegin(int state) {
        this.state = state;
    }

    /**
     * Returns the current lexical state.
     */
    @Override
    public int yystate() {
        return state;
    }

    @Override
    public int getTokenStart() {
        return tokenStart;
    }

    @Override
    public int getTokenEnd() {
        return tokenEnd;
    }

    @Override
    public void reset(CharSequence buf, int start, int end, int initialState) {
        this.buf = buf;
        this.tokenStart = start;
        this.tokenEnd = start;
        this.end = end;
        this.state = initialState;
    }

    /**
     * Resumes scanning until the next regular expression is matched,
     * the end of input is encountered or an I/O-Error occurs.
     *
     * @return the next token
     */
    @Override
    public IElementType advance() {
        if (tokenEnd >= end) {
            return null;
        }
        this.tokenStart = tokenEnd;
        this.tokenEnd = end;
        switch (state) {
            case START:
                return readMethod();
            case SEPARATOR:
                return readSeparator();
            case STRING:
                return readString();
            case STR_ESCAPE:
                return readStrEscape();
            case PARAM_START:
                return readParamStart();
            case PARAM_TYPE:
                return readParamType();
            case PARAM_NAME:
                return readParamName();
            case PARAM_END:
                return readParamEnd();
        }
        return LangTokenType.SPACE;
    }

    private IElementType skipSpace(boolean skipLine) {
        for (int i = tokenStart; i < end; i++) {
            char c = buf.charAt(i);
            if (c != ' ' && c != '\t') {
                if (c == '\n' && skipLine) {
                    continue;
                }
                tokenEnd = i;
                break;
            }
        }
        if (tokenEnd > tokenStart) {
            return LangTokenType.SPACE;
        }
        tokenEnd = tokenStart;
        return null;
    }

    private IElementType goError(int start) {
        for (int i = start; i < end; i++) {
            if (buf.charAt(i) == '\n') {
                tokenEnd = i + 1;
                break;
            }
        }
        state = START;
        return LangTokenType.ERROR;
    }

    private IElementType readMethod() {
        if (buf.charAt(tokenStart) == '#') {
            return readComment();
        }
        IElementType t = skipSpace(true);
        if (t != null) {
            return t;
        }
        for (int i = tokenStart; i < end; i++) {
            char c = buf.charAt(i);
            if (c == ' ' || c == '\t' || c == '=') {
                tokenEnd = i;
                break;
            } else if (!(c == '_' || c == '$'
                    || (c >= 'a' && c <= 'z')
                    || (c >= 'A' && c <= 'Z')
                    || (c >= '0' && c <= '9' && i > tokenStart))) {
                return goError(i);
            }
        }
        state = SEPARATOR;
        return LangTokenType.METHOD;
    }

    private IElementType readComment() {
        for (int i = tokenStart + 1; i < end; i++) {
            char c = buf.charAt(i);
            if (c == '\n') {
                state = START;
                tokenEnd = i;
                break;
            }
        }
        return LangTokenType.COMMENT;
    }

    private IElementType readSeparator() {
        IElementType t = skipSpace(false);
        if (t != null) {
            return t;
        }
        char c = buf.charAt(tokenStart);
        if (c == '=') {
            tokenEnd = tokenStart + 1;
            state = STRING;
            return LangTokenType.SEPARATOR;
        } else {
            return goError(tokenStart);
        }
    }

    private IElementType readString() {
        for (int i = tokenStart; i < end; i++) {
            char c = buf.charAt(i);
            if (c == '\n') {
                tokenEnd = i + 1;
                state = START;
                return LangTokenType.STRING;
            } else if (c == '{') {
                if (i > tokenStart && buf.charAt(i - 1) == '#') {
                    if (i - 1 == tokenStart) {
                        return readParamStart();
                    } else {
                        if (buf.charAt(i - 2) == '#') {
                            if (i - 2 == tokenStart) {
                                return readStrEscape();
                            } else {
                                tokenEnd = i - 2;
                                state = STR_ESCAPE;
                                return LangTokenType.STRING;
                            }
                        } else {
                            tokenEnd = i - 1;
                            state = PARAM_START;
                            return LangTokenType.STRING;
                        }
                    }
                }
            } else if (c == 'r' || c == 'n') {
                if (i - 2 >= tokenStart
                        && buf.charAt(i - 1) == '\\'
                        && buf.charAt(i - 2) == '\\') {
                    if (i - 2 == tokenStart) {
                        return readStrEscape();
                    } else {
                        tokenEnd = i - 2;
                        state = STR_ESCAPE;
                        return LangTokenType.STRING;
                    }
                } else if ((i - 1 >= tokenStart && buf.charAt(i - 1) == '\\')) {
                    if (i - 1 == tokenStart) {
                        tokenEnd = tokenStart + 1;
                        state = STRING;
                        return LangTokenType.STR_ESCAPE;
                    } else {
                        tokenEnd = i - 1;
                        state = STR_ESCAPE;
                        return LangTokenType.STRING;
                    }
                }
            }
        }
//        tokenEnd = end;
        return LangTokenType.STRING;
    }

    private IElementType readStrEscape() {
        tokenEnd = tokenStart + 2;
        state = STRING;
        return LangTokenType.STR_ESCAPE;
    }

    private IElementType readParamStart() {
        tokenEnd = tokenStart + 2;
        state = PARAM_TYPE;
        return LangTokenType.PARAM_START;
    }

    private IElementType readParamType() {
        IElementType t = skipSpace(false);
        if (t != null) {
            return t;
        }
        for (int i = tokenStart; i < end; i++) {
            char c = buf.charAt(i);
            if (c == ' ' || c == '\t') {
                if (isKeyword(buf.subSequence(tokenStart, i))) {
                    tokenEnd = i;
                    state = PARAM_NAME;
                    return LangTokenType.PARAM_TYPE;
                } else {
                    tokenEnd = i;
                    state = PARAM_END;
                    return LangTokenType.PARAM_NAME;
                }
            } else if (c == '}') {
                tokenEnd = i;
                state = PARAM_END;
                return LangTokenType.PARAM_NAME;
            }
        }
        return goError(tokenStart);
    }

    private IElementType readParamName() {
        IElementType t = skipSpace(false);
        if (t != null) {
            return t;
        }
        for (int i = tokenStart; i < end; i++) {
            char c = buf.charAt(i);
            if (c == ' ' || c == '\t' || c == '}') {
                tokenEnd = i;
                state = PARAM_END;
                return LangTokenType.PARAM_NAME;
            }
        }
        return goError(tokenStart);
    }

    private IElementType readParamEnd() {
        IElementType t = skipSpace(false);
        if (t != null) {
            return t;
        }
        if (buf.charAt(tokenStart) == '}') {
            tokenEnd = tokenStart + 1;
            state = STRING;
            return LangTokenType.PARAM_END;
        }
        return goError(tokenStart);
    }

    private static final String[] paramType = {"str", "num", "bool", "obj"};

    private boolean isKeyword(CharSequence s) {
        for (String kw : paramType) {
            if (kw.equals(s.toString())) return true;
        }
        if (s.charAt(0) == 'f') {
            try {
                Integer.parseInt(s.subSequence(1, s.length()).toString());
                return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

}
