# RESTLite i18n Plugin for IntelliJ IDEA
[![](https://img.shields.io/github/license/Yeamy/restlite-i18n-plugin)](https://github.com/Yeamy/restlite-i18n-plugin/blob/master/LICENSE) [![](https://img.shields.io/badge/market-jetbrains-black)](https://plugins.jetbrains.com/plugin/20268-restlite-i18n)  
[English](README.md) | 中文

这是一款 IntelliJ IDEA 的插件，帮助你轻松实现鸿蒙ArkTS`国际化`开发。
通过简单的自定义文件生成多语言ArkTS类，所有词条通过方法获取，支持参数类型限制，参数无顺序限制；

## 如何使用
1. 为你的module生成lang示例文件：Tools → RESTLite i18n → Generate ArkTS Lang File
2. 添加/修改lang文件，lang文件的语法都在示例文件中。
3. 根据lang文件生成ArkTS类: Tools → RESTLite i18n → Generate ArkTS Class
4. 重生成ArkTS类会覆盖存在旧的类，如果删除lang文件，请手动删除对应类。

## 最后
如果有bug请提交issue，我会尽快修复；
如果有功能需求或想法也请提交issue，我会尽量实现；

希望它对你有用。