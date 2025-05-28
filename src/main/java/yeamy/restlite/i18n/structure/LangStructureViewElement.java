package yeamy.restlite.i18n.structure;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import yeamy.restlite.i18n.edit.LangPsiElement;
import yeamy.restlite.i18n.edit.LangPsiFile;
import yeamy.restlite.i18n.edit.LangTokenType;

import java.util.ArrayList;
import java.util.List;

public class LangStructureViewElement implements StructureViewTreeElement, SortableTreeElement {

    private final NavigatablePsiElement myElement;
    private final String name;

    public LangStructureViewElement(NavigatablePsiElement element, String name) {
        this.myElement = element;
        this.name = name;
    }

    @Override
    public Object getValue() {
        return myElement;
    }

    @Override
    public void navigate(boolean requestFocus) {
        myElement.navigate(requestFocus);
    }

    @Override
    public boolean canNavigate() {
        return myElement.canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return myElement.canNavigateToSource();
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        return name;
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        ItemPresentation presentation = myElement.getPresentation();
        return presentation != null ? presentation : new PresentationData();
    }

    @Override
    public TreeElement @NotNull [] getChildren() {
        if (myElement instanceof LangPsiFile file) {
            List<LangStructureViewElement> out = new ArrayList<>();
            for (PsiElement child : file.getChildren()) {
                if (child.getNode().getElementType().equals(LangTokenType.METHOD)
                        && child instanceof LangPsiElement element) {
                    out.add(new LangStructureViewElement(element, getPresentation().getPresentableText()));
                }
            }
            return out.toArray(new TreeElement[0]);
        }
        return EMPTY_ARRAY;
    }

}