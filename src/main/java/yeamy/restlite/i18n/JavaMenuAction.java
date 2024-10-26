package yeamy.restlite.i18n;

import yeamy.restlite.i18n.lang.AbstractFile;

public class JavaMenuAction extends AbstractClassMenuAction {

    @Override
    protected String fileExtension() {
        return ".java";
    }

    @Override
    protected byte[] getFileData(AbstractFile<?> f) {
        return f.javaSource().getBytes();
    }

}
