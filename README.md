# project-model
1分钟搭建springboot+mybatis-plus+swagger+log4j2+动态数据源配置 + aop日志配置 架构

架构介绍: 该架构整合了springboot+mybatis-plus+swagger+log4j2+动态数据源配置 + aop日志配置，这里的配置文件都无需手动填写，只要点击运行生成器即可自动生成所有的配置文件及配置类。 傻瓜式操作即可搭建一套完整的springboot架构体系。你只需要做如下一个步骤即可：

    1.修改项目名称(需要开发的新项目名称)
    
    2.运行生成器初始化生成项目配置
    
    3.填写你的数据库连接到application-dev.yml文件中并运行mybatis逆向生成工具，生成你想要的数据库表对应的Java文件类
    
    4.运行项目，本地打开http://localhost:8910/swagger-ui.html即可查看swagger生成效果。
    
    
