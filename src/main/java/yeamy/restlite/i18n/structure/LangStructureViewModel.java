package yeamy.restlite.i18n.structure;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import yeamy.restlite.i18n.edit.LangPsiElement;
import yeamy.restlite.i18n.edit.LangPsiFile;

public class LangStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {

    public LangStructureViewModel(@Nullable Editor editor, PsiFile psiFile) {
        super(psiFile, editor, new LangStructureViewElement(psiFile, psiFile.getName()));
    }

    @NotNull
    public Sorter @NotNull [] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }


    @Override
    public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
        return false;
    }

    @Override
    public boolean isAlwaysLeaf(StructureViewTreeElement element) {
        return !(element.getValue() instanceof LangPsiFile);
    }

    @Override
    protected Class<?> @NotNull [] getSuitableClasses() {
        return new Class[]{LangPsiElement.class};
    }

}