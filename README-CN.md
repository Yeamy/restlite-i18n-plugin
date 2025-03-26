# RESTLite i18n Plugin for IntelliJ IDEA
[![](https://img.shields.io/github/license/Yeamy/restlite-i18n-plugin)](https://github.com/Yeamy/restlite-i18n-plugin/blob/master/LICENSE) [![](https://img.shields.io/badge/market-jetbrains-black)](https://plugins.jetbrains.com/plugin/20268-restlite-i18n)  
[English](README.md) | 中文

这是一款 IntelliJ IDEA 的插件，帮助你轻松实现JAVA`国际化`开发。  
其灵感来源于Mybatis，通过简单的自定义文件生成多语言Java类。

## 有何不同
1. 采用方法调用的方式获取，而不是其他框架的键值对查找，String查找替换的方式；
2. 支持参数传入，支持参数类型限制，参数无顺序限制；
3. 可以通过HttpServletRequest自动选择委托类。

## 如何使用
1. 为你的module生成lang示例文件：Tools → RESTLite i18n → Generate Lang File
2. 添加/修改lang文件，lang文件的语法都在示例文件中。
3. 根据lang文件生成java类: Tools → RESTLite i18n → Generate Java Class
4. 重生成Java类会覆盖存在旧的类，如果删除lang文件，请手动删除对应类。

## Q & A
**Q**：为什么采用插件而不是框架  
**A**：因为项目的设计初衷是通过自定义文件创建Java类，从而避免大量转义符带来的杂乱感， 而Java APT无法获取类以为的资源文件，故只能使用插件形式。

**Q**：为什么使用lang自定义文件而不是其他现有格式  
**A**：该插件的初衷之一是为了避免大量转义符影响阅读，lang文件的转义符只有“##{”，“\\n”和“\\r”三个，大大降低了转义符带来的阅读困难。

**Q**：能否在Android上使用  
**A**：该插件只是生成Java类与方法并不会限制其使用平台，但是由于Android方法数限制，请慎用。

## 最后
如果有bug请提交issue，我会尽快修复；
如果有功能需求或想法也请提交issue，我会尽量实现；

希望它对你有用。