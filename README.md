离线转译 web 服务
================

![Java Version Badge][Java Version Badge]
[![Screwdriver Badge][Screwdriver Badge]][Screwdriver URL]

业务流程图
----------

<div align="center">
    <img src="./docs/业务流程图.png" />
</div>

开发指南
--------

### 获取代码

#### IntelliJ 配置自动代码风格

目前，我们制定并完善了本项目的代码风格配置，并将其作为IntelliJ设置。如果使用IntelliJ作为IDE，我们可以通过导入仓库根目录中的[jersey-webservice-template-Project-intellij-code-style.xml][style config]文件来导入这些代码样式设置。项目设置将作为名为“jersey-webservice-template-Project”的新方案出现在IDE的**Editor** -> **Code Style**部分下。

请同时启用“移除未使用的导入”功能，通过**Editor** -> **General** -> **Auto Import** -> **Optimize Imports on the Fly**，它将自动移除未使用的导入。

##### IntelliJ问题解决

###### IntelliJ无法读取资源文件

当我们在IntelliJ中运行单元测试时，有时会遇到错误，提示“某个资源文件”无法找到。我们知道路径绝对正确。如果是这种情况，这只是IntelliJ的问题，通过明确告诉IntelliJ这些资源的位置来解决：

![Error loading intelliJ-find-resource.png](docs/intelliJ-find-resource.png)

###### 制表符宽度

我们使用4个空格作为制表符。这可以在**Code Style** -> **Java** -> **Tabs and Indents**中进行配置，设置如下：

Tab size: 4
Indent: 4
Continuation indent: 8

如果按下TAB或Enter时制表符仍然以2个空格出现，而不是4个空格，请尝试：

1. “Settings -> Editor ->  Code Style” -- 如果启用了“Detect and use existing file indents for editing”，请尝试禁用它（默认是启用的）。注意：可能需要重新打开编辑器中的文件。
2. 在你的文件路径中是否有任何.editorconfig文件？来自.editorconfig的设置（“Settings ->  Editor ->  Code Style”）具有优先级（将覆盖）你的IDE设置。

### 打包编译

### 本地启动

[Java Version Badge]: https://img.shields.io/badge/Java-17-brightgreen?style=for-the-badge&logo=OpenJDK&logoColor=white
