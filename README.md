# RestLite i18n Plugin for IntelliJ IDEA

English | [中文](README-CN.md)

This is a plugin for IntelliJ IDEA, helps you with JAVA International development.

### What's different
Unlike other frameworks, This plugin choose a different way: 
1. Generate method rather than using key-value;
2. Parameter supported, type limit supported，No order restriction for parameters;
3. Automatically select delegate class by HttpServletRequest.

### How to use
1. Generate lang sample file for your module: Tools → RestLite I18n → Generate Lang File
2. Modify your lang file in restlite-i18n(the same level as the src directory), Grammar of lang-file are all in the sample file.
3. Generate java class by lang file: Tools → RestLite I18n → Generate Java Class
4. Regenerate java class will overwrite the olds. If you delete the lang file, please delete the class by yourself.

### At Last
Welcome to commit bug / new feature.
Wish it work for you.