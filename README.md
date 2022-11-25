# RestLite i18n Plugin for IntelliJ IDEA
[![](https://img.shields.io/github/license/Yeamy/restlite-i18n-plugin)](https://github.com/Yeamy/restlite-i18n-plugin/blob/master/LICENSE) [![](https://img.shields.io/badge/market-jetbrains-black)](https://plugins.jetbrains.com/plugin/20268-restlite-i18n)  
English | [中文](README-CN.md)

This is a plugin for IntelliJ IDEA, helps you with JAVA International development.

### What's different
1. All words generate by methods rather than using Map and String.format;
2. Parameter supported, type limit supported，No order restriction for parameters;
3. Automatically select delegate class by HttpServletRequest.

### How to use
1. Generate lang sample file for your module: Tools → RestLite I18n → Generate Lang File
2. Add/Modify your lang files, All Syntax of lang-file are all in the sample file.
3. Generate java class from lang file: Tools → RestLite I18n → Generate Java Class
4. Regenerate java class will overwrite the olds. If you delete the lang file, please delete the class by yourself.

## Version Update
### 2.0
- The generated lang files is saved in the selected java package.
- Add icon for menu and lang file.
- Syntax highlighter for lang files.
- Less escape character, only: ##{, \\n and \\r.
### 1.1
- Less escape character: typing #{ with ##{ instead; ~~no need to add escape character for " nor \ unless \b, \t, \n, \f, \r, \\ appear.~~

## Update Plan
- Compile lang file to class.(PS: IDEA doc not complete yet)
- ~~Syntax highlighter.(done)~~

### At Last
Welcome to commit bug / new feature.
Wish it work for you.
