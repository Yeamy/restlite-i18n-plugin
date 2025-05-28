package yeamy.restlite.i18n.edit;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.icons.AllIcons;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class LangPsiElement extends ASTWrapperPsiElement {
    private ItemPresentation presentation;

    public LangPsiElement(ASTNode astNode) {
        super(astNode);
    }

    @Override
    public ItemPresentation getPresentation() {
        if (presentation == null) {
            presentation = new ItemPresentation() {
                @Override
                public @NlsSafe @Nullable String getPresentableText() {
                    return getName();
                }

                @Override
                public @Nullable Icon getIcon(boolean unused) {
                    return AllIcons.Nodes.Method;
                }
            };
        }
        return presentation;
    }

    @Override
    public String getName() {
        if (!getNode().getElementType().equals(LangTokenType.METHOD)) return super.getName();
        for (PsiElement child : getChildren()) {
            if (child.getNode().getElementType().equals(LangTokenType.METHOD_NAME)) {
                return child.getText();
            }
        }
        return "< ... >";
    }

}
