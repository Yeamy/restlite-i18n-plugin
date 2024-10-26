package yeamy.restlite.i18n;

import yeamy.restlite.i18n.lang.AbstractFile;

public class KotlinMenuAction extends AbstractClassMenuAction {

    @Override
    protected String fileExtension() {
        return ".kt";
    }

    @Override
    protected byte[] getFileData(AbstractFile<?> f) {
        return f.kotlinSource().getBytes();
    }

}
