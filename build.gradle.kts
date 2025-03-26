plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.3.0"
    id("org.jetbrains.intellij.platform.migration") version "2.3.0"
}

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }

}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
intellijPlatform {

    pluginConfiguration {
        id = "yeamy.restlite.i18n"
        name = "RESTLite i18n"
        version = "2.3"
        description = """
    · This plugin helps you with <b>JAVA International</b> development.<br>
    · Create simple customization files and generate java/kotlin class with it. All words obtained through methods rather than string format.<br>
    · 该插件可以帮助你轻松实现<b>JAVA国际化</b>开发。<br>
    · 通过简单的自定义文件生成多语言Java/Kotlin类，所有词条通过方法获取，而非Map<> + String.format形式。<br>
    <br>
    <b>How to use:</b><br>
    <b>1.</b> Generate *.lang file for your project/module: Tools → RESTLite i18n → Generate Java Lang File<br>
    <b>2.</b> Add and modify your lang files, the syntax of lang-file are all in the sample file.<br>
    <b>3.</b> Generate *.java class by lang file: Tools → RESTLite i18n → Generate Java Class<br>
    <b>4.</b> Regenerate java class will overwrite the olds. If you delete the lang file, please delete the class by yourself.<br>
    <b>如何使用：</b><br>
    <b>1、</b>为你的项目生成lang文件: Tools → RESTLite i18n → Generate Java Lang File<br>
    <b>2、</b>添加并修改lang文件，lang文件的语法都在示例文件中。<br>
    <b>3、</b>根据lang文件生成java类: Tools → RESTLite i18n → Generate Java Class<br>
    <b>4、</b>重生成Java类会覆盖存在旧的类，如果删除lang文件，请手动删除对应类。<br>
    <br>
        """.trimIndent()
        changeNotes = """
        <b>2.3</b><ul>
            <li>Change parameter's color of sentence.</li>
            <li>Add boolean supported.</li>
            <li>Add language parameter in build.lang file.</li>
            <li>Add 'util' to replace 'proxy' in build.lang file.</li>
        </ul>
        <b>2.2</b><ul>
            <li>Show choose dialog when the package exists more than one directory.</li>
        </ul>
        <b>2.1</b><ul>
            <li>Fix bug.</li>
            <li>Support kotlin.</li>
            <li>Support javax-Servlet and jakarta-Servlet.</li>
        </ul>
        <b>2.0</b><ul>
            <li>The generated lang files is saved in the selected java package.</li>
            <li>Add icon for menu and lang file.</li>
            <li>Syntax highlighter for lang files.</li>
            <li>Less escape character, only: ##{ \\n \\r.</li>
        </ul>
        <b>-------------------------------------</b><br>
        <b>2.3</b><ul>
            <li>修改词条参数的颜色</li>
            <li>支持boolean类型</li>
            <li>build文件新增language参数</li>
            <li>build文件新增util取代proxy</li>
        </ul>
        <b>2.2</b><ul>
            <li>当同个包名存在多个目录时弹出选择框.</li>
        </ul>
        <b>2.1</b><ul>
            <li>修改bug.</li>
            <li>支持生成kotlin.</li>
            <li>支持javax Servlet 和 jakarta Servlet.</li>
        </ul>
        <b>2.0</b><ul>
            <li>生成的lang文件直接保存在java package中。</li>
            <li>给菜单和lang文件添加图标。</li>
            <li>lang文件提供语法高亮显示。</li>
            <li>更少的转义符，只有: ##{ \\n \\r.</li>
        </ul>
        """.trimIndent()

        ideaVersion {
            sinceBuild = "242"
            untilBuild = provider { null }
        }

        vendor {
            name = "Yeamy"
            email = "yeamy0754@hotmail.com"
            url = "https://github.com/yeamy/restlite-i18n-plugin"
        }

    }

    publishing {
        token = System.getenv("PUBLISH_TOKEN")
    }

    signing {
        certificateChain = System.getenv("CERTIFICATE_CHAIN")
        privateKey = System.getenv("PRIVATE_KEY")
        password = System.getenv("PRIVATE_KEY_PASSWORD")
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.3.4")
        bundledPlugin("com.intellij.java")
    }
}
