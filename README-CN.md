# RestLite i18n Plugin for IntelliJ IDEA

[English](README.md) | 中文

这是一款 IntelliJ IDEA 的插件，为你提供Java国际化开发的解决方案。

其灵感来源于Mybatis，通过简单的自定义文件生成多语言Java类。

### 有何不同
不同于以往的其他框架，本插件选择与众不同的方式实现：
1. 采用方法调用的方式获取，而不是其他框架的键值对查找；
2. 支持参数传入，支持参数类型限制，参数无顺序限制；
3. 可以通过HttpServletRequest自动选择委托类

### 如何使用
1. 为你的module生成lang示例文件：Tools → RestLite I18n → Generate Lang File
2. 修改lang文件（文件在src同级目录下的restlite-i18n目录内)，lang文件的语法都在示例文件中。
3. 根据lang文件生成java类: Tools → RestLite I18n → Generate Java Class
4. 重生成Java类会覆盖存在旧的类，如果删除lang文件，请手动删除对应类。

### Q & A
Q：为什么采用插件而不是框架  
A：因为项目的设计初衷是通过自定义文件创建Java类，从而避免大量转义符带来的杂乱感， 而Java APT无法获取类以为的资源文件，故只能使用插件形式。

Q：为什么使用lang自定义文件而不是其他现有格式  
A：为了避免转义符，lang文件的转义符只有“##{”唯一一个；引号不需要转义符；除非后面跟着b、t、n、f、r、\等字符，单个反斜杆也不需要转义符。

Q：能不能像Lambok一样实现自动化代码生成  
A：Lambok其运行是通过监听的带项目信息的内部接口触发，而官方并未对外提供该接口，而其他开放接口并不提供项目信息，也就无法操作文件，故无法实现。

Q：能否在Android上使用  
A：该插件只是生成Java类与方法并不会限制其使用平台， 但是由于Android方法数限制问题，不建议在Android上使用该方式。

### 更新计划
为lang文件编辑提供语法检查。

### 最后
如果有bug请提交issue，我会尽快修复；
如果有功能需求或想法也请提交issue，我会尽量实现；

希望它对你有用。