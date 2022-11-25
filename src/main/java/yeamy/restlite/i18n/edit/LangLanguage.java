package yeamy.restlite.i18n.edit;

import com.intellij.lang.Language;

public class LangLanguage extends Language {

  public static final LangLanguage INSTANCE = new LangLanguage();

  private LangLanguage() {
    super("Lang");
  }

}
