package yeamy.restlite.i18n;

import com.intellij.icons.AllIcons;
import com.intellij.ide.IdeCoreBundle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.TreeSpeedSearch;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.Objects;

public class DirectoryChooserDialog extends DialogWrapper {

    private final @NotNull Project myProject;

    private Tree myTree;

    private DefaultTreeModel myModel;

    public DirectoryChooserDialog(@NlsContexts.DialogTitle @NotNull String title, @NotNull Project project) {
        super(project, true);
        setTitle(title);
        myProject = project;
        init();
    }

    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        myModel = new DefaultTreeModel(new DirNode(myProject.getName(), null));
        createTreeModel();
        myTree = new Tree(myModel);
        myTree.setCellRenderer(new ColoredTreeCellRenderer() {
            @Override
            public void customizeCellRenderer(
                    @NotNull JTree tree,
                    Object value,
                    boolean selected,
                    boolean expanded,
                    boolean leaf,
                    int row,
                    boolean hasFocus
            ) {
                if (value instanceof DirNode node) {
                    VirtualFile dir = node.getUserObject();
                    if (dir == null) {
                        append(IdeCoreBundle.message("node.default"));
                        setIcon(AllIcons.Actions.ModuleDirectory);
                    } else {
                        append(node.name());
                        if (node.name().endsWith("/ets")) {
                            setIcon(AllIcons.Modules.SourceRoot);
                        } else {
                            setIcon(AllIcons.Nodes.Folder);
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = ScrollPaneFactory.createScrollPane(myTree);
        scrollPane.setPreferredSize(JBUI.size(500, 300));

        TreeSpeedSearch.installOn(myTree, canExpandInSpeedSearch(), path -> {
            DirNode node = (DirNode) path.getLastPathComponent();
            VirtualFile object = node.getUserObject();
            return (object == null) ? "" : object.getName();
        });

        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel northPanel = new JPanel(new BorderLayout(8, 0));
        northPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        panel.add(northPanel, BorderLayout.NORTH);
        return panel;
    }

    protected boolean canExpandInSpeedSearch() {
        return false;
    }

    @Override
    public String getDimensionServiceKey() {
        return "#com.intellij.ide.util.DirectoryChooserDialog";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return myTree;
    }

    public VirtualFile getSelectedDirectory() {
        if (getExitCode() == CANCEL_EXIT_CODE) return null;
        return getTreeSelection();
    }

    private @Nullable VirtualFile getTreeSelection() {
        if (myTree == null) return null;
        TreePath path = myTree.getSelectionPath();
        if (path == null) return null;
        DirNode node = (DirNode) path.getLastPathComponent();
        return node.getUserObject();
    }

    private void createTreeModel() {
        VirtualFile @NotNull [] dirs = ProjectRootManager.getInstance(myProject).getContentRoots();
        for (VirtualFile dir : dirs) {
            addDirectory(dir);
        }
        TreeUtil.sort(myModel, (o1, o2) -> {
            VirtualFile element1 = ((DirNode) o1).getUserObject();
            VirtualFile element2 = ((DirNode) o2).getUserObject();
            return Objects.requireNonNull(element1.getName()).compareToIgnoreCase(Objects.requireNonNull(element2.getName()));
        });
    }

    private void addDirectory(@NotNull VirtualFile dir) {
        String name = dir.getName();
        DirNode rootNode = (DirNode) myModel.getRoot();
        if (name.equals(myProject.getName())) {
            rootNode.setUserObject(dir);
            addChildDirectory(rootNode, dir);
        }
    }

    private void addChildDirectory(DirNode node, VirtualFile ets) {
        for (VirtualFile vf : ets.getChildren()) {
            if (vf.isDirectory() && vf.getName().charAt(0) != '.') {
                DirNode childNode = new DirNode(vf.getName(), vf);
                node.add(childNode);
                addChildDirectory(childNode, vf);
            }
        }
    }

    public static class DirNode extends DefaultMutableTreeNode {
        private final String name;

        public DirNode(String name, VirtualFile dir) {
            super(dir);
            this.name = name;
        }

        @Override
        public VirtualFile getUserObject() {
            return (VirtualFile) userObject;
        }

        public String name() {
            return name;
        }
    }
}

