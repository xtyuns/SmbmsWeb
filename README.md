## 项目简介
本项目为 Java Web 原生项目, 采用 Servlet + JDBC 技术, 仅供入门学习


## 在 IDEA 中运行本项目
##### 首先使用 IDEA 打开本项目

##### 导入数据库
将 smbms.sql 导入进数据库, 并在 ```src\main\resources\mysql.properties``` 文件中配置数据连接信息

##### 配置 Project Structure
1. 打开 Project Structure ```(Ctrl + Alt + Shift + S)```
2. 在 Facets 中添加 Web Facet
3. 创建 Artifacts -> Web Application
4. 在 Artifacts 中双击右侧 jar 包, 将项目中的 jar 包添加到 output root

##### 设置编码
1. 设置 IDEA 编码: 打开菜单栏 ```Help```->```Edit Custom VM Options``` 新增一行配置内容 ```-Dfile.encoding=utf-8```
2. 配置 Tomcat, 并在 VM options 中填入 ```-Dfile.encoding=utf-8```

##### 启动并访问项目