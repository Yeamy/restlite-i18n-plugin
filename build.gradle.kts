plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.3.0"
//    id("org.jetbrains.intellij.platform.migration") version "2.3.0"
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
        id = "yeamy.restlite.i18n.go"
        name = "RESTLite i18n"
        version = "1.0.1"
        description = """
    · This plugin helps you with Go <b>International</b> development.<br>
    · Create simple customization files and generate go source file with it. All sentences obtained through methods.<br>
    <br>
    · 该插件可以帮助你轻松实现Go<b>多语言国际化</b>开发。<br>
    · 通过简单的自定义文件生成多地区语言Go源码，所有词条通过方法获取。<br>
    <br>
    <b>How to use:</b><br>
    <b>1.</b> Generate *.lang file for your project/module: Tools → RESTLite i18n → Generate Go Lang File<br>
    <b>2.</b> Add and modify your lang files, the syntax of lang-file are all in the sample file.<br>
    <b>3.</b> Generate *.go source file by lang file: Tools → RESTLite i18n → Generate Go Source<br>
    <b>4.</b> Regenerate source file will overwrite the olds. If you delete the lang file, please delete the source file by yourself.<br>
    <br>
    <b>如何使用：</b><br>
    <b>1、</b>为你的项目生成*.lang文件: Tools → RESTLite i18n → Generate Go Lang File<br>
    <b>2、</b>添加并修改lang文件，lang文件的语法都在示例文件中。<br>
    <b>3、</b>根据lang文件生成*.go源码: Tools → RESTLite i18n → Generate Go Source<br>
    <b>4、</b>重生成Go源码会覆盖存在旧的源码文件，如果删除lang文件，请手动删除对应源码文件。<br>
    <br>
        """.trimIndent()
        changeNotes = """
    <b>1.0.1</b><ul>
        <li>New tree Directory Picker Dialog.</li>
    </ul>
        """.trimIndent()

        ideaVersion {
            sinceBuild = "223"
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
        goland("2024.3.4")
    }
}
