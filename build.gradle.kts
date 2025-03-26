plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.3.0"
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
        id = "yeamy.restlite.i18n.ets"
        name = "RESTLite i18n (ArkTS)"
        version = "1.0"
        description = """
    该插件可以帮助你轻松实现鸿蒙ArkTS<b>国际化</b>开发。<br>
    通过简单的自定义文件生成多语言ArkTS类，所有词条通过方法获取。<br>
    This plugin helps you with Harmony ArkTS <b>International</b> development.<br>
    Create simple customization files and generate ArkTS class with it. All sentences obtained through methods.<br><b>如何使用：</b><br>
    <br>
    <b>1、</b>为你的项目生成lang文件: Tools → RESTLite i18n → Generate ArkTS Lang File<br>
    <b>2、</b>添加并修改lang文件，lang文件的语法都在示例文件中。<br>
    <b>3、</b>根据lang文件生成ArkTS类: Tools → RESTLite i18n → Generate ArkTS Class<br>
    <b>4、</b>重生成ArkTS类会覆盖存在旧的类，如果删除lang文件，请手动删除对应类。<br>
    <b>How to use:</b><br>
    <b>1.</b> Generate *.lang file for your project/module: Tools → RESTLite i18n → Generate ArkTS Lang File<br>
    <b>2.</b> Add and modify your lang files, the syntax of lang-file are all in the sample file.<br>
    <b>3.</b> Generate ArkTS class by lang file: Tools → RESTLite i18n → Generate ArkTS Class<br>
    <b>4.</b> Regenerate ArkTS class will overwrite the olds. If you delete the lang file, please delete the class by yourself.<br>
    <br>
        """.trimIndent()
        changeNotes = ""

        ideaVersion {
            sinceBuild = "233"
            untilBuild = provider { null }
        }

        vendor {
            name = "Yeamy"
            email = "yeamy0754@hotmail.com"
            url = "https://github.com/Yeamy/restlite-i18n-plugin/tree/ets"
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
        local("/Applications/DevEco-Studio.app")
//        bundledPlugin("com.intellij.java")
//        intellijIdeaCommunity("2024.3.4")
    }
}
