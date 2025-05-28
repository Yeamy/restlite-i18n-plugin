import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.ParsingTestCase;
import yeamy.restlite.i18n.edit.LangParserDefinition;

public class MyParserTest extends ParsingTestCase {
    public MyParserTest() {
        super("", "lang", new LangParserDefinition());
    }

    @Override
    protected String getTestDataPath() {
        return "testData";
    }

    public void testParserTriggersCreateElement() {
        // 解析测试文件
        PsiFile file = parseFile("build",
                """
                        #RESTLite i18n configuration
                        language=go
                        
                        #Name of package
                        package=i18n
                        
                        #Name of source file
                        file=
                        i18n.go
                        
                        #Default language/locate(see more about http header Accept-Language: https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Language)
                        default=zh-CN
                        
                        #Set if generate auto-select-method with param "net/http"
                        http=true
                        
                        """);

        // 强制访问所有 Psi 元素
        visitAllPsiElements(file);

        // 断言解析成功
        assertNotNull(file);
    }

    private void visitAllPsiElements(PsiElement element) {
        for (PsiElement child : element.getChildren()) {
            visitAllPsiElements(child);
        }
    }
}