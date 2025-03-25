# RESTLite i18n Plugin for IntelliJ IDEA
[![](https://img.shields.io/github/license/Yeamy/restlite-i18n-plugin)](https://github.com/Yeamy/restlite-i18n-plugin/blob/master/LICENSE) [![](https://img.shields.io/badge/market-jetbrains-black)](https://plugins.jetbrains.com/plugin/26895-restlite-i18n)  
English | [中文](README-CN.md)

This is a plugin for IntelliJ IDEA, helps you with GO `International` development.
Generate Go source code through simple custom files and use method calls to obtain sentence;

### What's different
1. All sentences generate by methods rather than using Map and String.format;
2. Parameter supported, type limit supported，No order restriction for parameters;

### How to use
1. Generate lang sample file for your module: Tools → RESTLite i18n → Generate Go Lang File
2. Add/Modify your lang files, All Syntax of lang-file are all in the sample file.
3. Generate go source file from lang file: Tools → RESTLite i18n → Generate Go Source
4. Regenerate go source file will overwrite the olds. If you delete the lang file, please delete the source file  by yourself.

## Version Update
### 1.0
- transplant the plugin from java to go
- support go build-in type
### 1.0
- new tree directory picker.

### At Last
Welcome to commit bug / new feature.
Wish it work for you.
