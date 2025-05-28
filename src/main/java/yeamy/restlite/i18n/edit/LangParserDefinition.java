package yeamy.restlite.i18n.edit;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

public class LangParserDefinition implements ParserDefinition {

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new FlexAdapter(new LangFlexLexer());
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new LangPsiParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return LangFileElementType.INSTANCE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode astNode) {
        return new LangPsiElement(astNode);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider fileViewProvider) {
        return new LangPsiFile(fileViewProvider);
    }
}
