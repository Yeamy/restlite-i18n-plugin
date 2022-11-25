package yeamy.restlite.i18n.edit;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class LangFileType extends LanguageFileType {

  public static final LangFileType INSTANCE = new LangFileType();

  private LangFileType() {
    super(LangLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "Lang File";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "RESTlite i18n language file";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "lang";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return LangIcons.FILE;
  }

}
