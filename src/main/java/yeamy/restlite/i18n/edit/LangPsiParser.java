package yeamy.restlite.i18n.edit;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class LangPsiParser implements PsiParser, LightPsiParser {

    @Override
    public @NotNull ASTNode parse(@NotNull IElementType iElementType, @NotNull PsiBuilder psiBuilder) {
        psiBuilder.setDebugMode(true);
        parseLight(iElementType, psiBuilder);
        return psiBuilder.getTreeBuilt();
    }

    @Override
    public void parseLight(IElementType t, PsiBuilder b) {
        final PsiBuilder.Marker root = b.mark();
        PsiBuilder.Marker method = null;
        while (!b.eof()) {
            IElementType type = b.getTokenType();
            if (type != null) {
                if (type.equals(LangTokenType.METHOD_NAME)) {
                    if (method != null) method.done(LangTokenType.METHOD);
                    method = b.mark();
                } else if (method != null && (type.equals(LangTokenType.ERROR)
                        || type.equals(LangTokenType.COMMENT)
                        || type.equals(LangTokenType.CRLF))) {
                    method.done(LangTokenType.METHOD);
                    method = null;
                }
                PsiBuilder.Marker mark = b.mark();
                b.advanceLexer();
                mark.done(type);
            }
        }
        if (method != null && !method.isCollapsed()) method.done(LangTokenType.METHOD);
        b.advanceLexer();
        root.done(t);
    }

}
