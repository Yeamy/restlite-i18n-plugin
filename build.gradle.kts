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
        id = "yeamy.restlite.i18n.go"
        name = "RESTLite i18n"
        version = "1.0"
        description = """
    This plugin helps you with <b>Go International</b> development.<br>
    Create simple customization files and generate go class with it. All words obtained through methods.<br>
    Wish it useful for you.<br>
    该插件可以帮助你轻松实现<b>Go国际化</b>开发。<br>
    通过简单的自定义文件生成多语言Go类，所有词条通过方法获取。<br>
    希望它对你有用。<br>
    <b>How to use:</b><br>
    <b>1.</b> Generate *.lang file for your project/module: Tools → RESTLite i18n → Generate Lang File<br>
    <b>2.</b> Add and modify your lang files, the syntax of lang-file are all in the sample file.<br>
    <b>3.</b> Generate *.go class by lang file: Tools → RESTLite i18n → Generate Go Class<br>
    <b>4.</b> Regenerate class will overwrite the olds. If you delete the lang file, please delete the class by yourself.<br>
    <b>如何使用：</b><br>
    <b>1、</b>为你的项目生成lang文件: Tools → RESTLite i18n → Generate Lang File<br>
    <b>2、</b>添加并修改lang文件，lang文件的语法都在示例文件中。<br>
    <b>3、</b>根据lang文件生成go类: Tools → RESTLite i18n → Generate Go Class<br>
    <b>4、</b>重生成类会覆盖存在旧的类，如果删除lang文件，请手动删除对应类。<br>
    <br>
        """.trimIndent()
        changeNotes = ""

        ideaVersion {
            sinceBuild = "243"
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
