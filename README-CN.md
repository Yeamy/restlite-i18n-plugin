# RESTLite i18n Plugin for IntelliJ IDEA
[![](https://img.shields.io/github/license/Yeamy/restlite-i18n-plugin)](https://github.com/Yeamy/restlite-i18n-plugin/blob/master/LICENSE) [![](https://img.shields.io/badge/market-jetbrains-black)](https://plugins.jetbrains.com/plugin/26895-restlite-i18n)  
[English](README.md) | 中文

这是一款 IntelliJ IDEA 的插件，帮助你轻松实现Go`国际化`开发。  
通过简单的自定义文件生成多语言Go源码，使用方法调用的方式来获取词条。

## 有何不同
1. 采用方法调用的方式获取，而不是其他框架的键值对查找，String查找替换的方式；
2. 支持参数传入，支持参数类型限制，参数无顺序限制；

## 如何使用
1. 为你的module生成lang示例文件：Tools → RESTLite i18n → Generate Go Lang File
2. 添加/修改lang文件，lang文件的语法都在示例文件中。
3. 根据lang文件生成Go源码: Tools → RESTLite i18n → Generate Go Source
4. 重生成Go源码会覆盖存在旧的源码文件，如果删除lang文件，请手动删除对应源码文件。

## 版本升级
### 1.0
- 将插件从java版移植到go语言版本
- 支持go内置类型
### 1.0.1
- 升级生成lang模板文件的目录选择器

## 最后
如果有bug请提交issue，我会尽快修复；
如果有功能需求或想法也请提交issue，我会尽量实现；

希望它对你有用。