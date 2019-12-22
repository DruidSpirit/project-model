# 共有8个级别，按照从低到高为：ALL < TRACE < DEBUG < INFO < WARN < ERROR < FATAL < OFF。

Configuration:
  status: warn
  monitorInterval: 30

  Properties: # 定义全局变量
    Property: # 缺省配置（用于开发环境）。其他环境需要在VM参数中指定，如下：
    #测试：-Dlog.level.console=warn -Dlog.level.xjj=trace
    #生产：-Dlog.level.console=warn -Dlog.level.xjj=info
    - name: log.level.mapper
      value: debug
    - name: log.level.console
      value: info
    - name: log.path
      value: D:/log4j2/
    - name: project.name
      value: ${cfg.projectInfo.moduleName}
    - name: log.pattern
      value: "%d{yyyy-MM-dd HH:mm:ss.SSS} -%5p ${r"${PID:-}"} [%15.15t] %-30.30C{1.} : %m%n"

  Appenders:
    Console:  #输出到控制台
      name: CONSOLE
      target: SYSTEM_OUT
      PatternLayout:
        pattern: ${r"${log.pattern}"}
    #   启动日志
    RollingFile:
    - name: ROLLING_FILE
      fileName: ${r"${log.path}/${project.name}.log"}
      filePattern: "${r"${log.path}/historyRunLog/$${date:yyyy-MM}/${project.name}-%d{yyyy-MM-dd}-%i.log.gz"}"
      PatternLayout:
        pattern: ${r"${log.pattern}"}
      Filters:
        #        一定要先去除不接受的日志级别，然后获取需要接受的日志级别
        ThresholdFilter:
        - level: error
          onMatch: DENY
          onMismatch: NEUTRAL
        - level: info
          onMatch: ACCEPT
          onMismatch: DENY
      Policies:
        TimeBasedTriggeringPolicy:  # 按天分类
          modulate: true
          interval: 1
      DefaultRolloverStrategy:     # 文件最多100个
        max: 100
    #   平台日志
    - name: PLATFORM_ROLLING_FILE
      ignoreExceptions: false
      fileName: ${r"${log.path}/platform/${project.name}_platform.log"}
      filePattern: "${r"${log.path}/platform/$${date:yyyy-MM}/${project.name}-%d{yyyy-MM-dd}-%i.log.gz"}"
      PatternLayout:
        pattern: ${r"${log.pattern}"}
      Policies:
        TimeBasedTriggeringPolicy:  # 按天分类
          modulate: true
          interval: 1
      DefaultRolloverStrategy:     # 文件最多100个
        max: 100
    #   业务日志
    - name: BUSSINESS_ROLLING_FILE
      ignoreExceptions: false
      fileName: ${r"${log.path}/bussiness/${project.name}_bussiness.log"}
      filePattern: "${r"${log.path}/bussiness/$${date:yyyy-MM}/${project.name}-%d{yyyy-MM-dd}-%i.log.gz"}"
      PatternLayout:
        pattern: ${r"${log.pattern}"}
      Policies:
        TimeBasedTriggeringPolicy:  # 按天分类
          modulate: true
          interval: 1
      DefaultRolloverStrategy:     # 文件最多100个
        max: 100
    #   错误日志
    - name: EXCEPTION_ROLLING_FILE
      ignoreExceptions: false
      fileName: ${r"${log.path}/exception/${project.name}_exception.log"}
      filePattern: "${r"${log.path}/exception/$${date:yyyy-MM}/${project.name}-%d{yyyy-MM-dd}-%i.log.gz"}"
      ThresholdFilter:
        level: error
        onMatch: ACCEPT
        onMismatch: DENY
      PatternLayout:
        pattern: ${r"${log.pattern}"}
      Policies:
        TimeBasedTriggeringPolicy:  # 按天分类
          modulate: true
          interval: 1
      DefaultRolloverStrategy:     # 文件最多100个
        max: 100
    #   DB 日志
    - name: DB_ROLLING_FILE
      ignoreExceptions: false
      fileName: ${r"${log.path}/db/${project.name}_db.log"}
      filePattern: "${r"${log.path}/db/$${date:yyyy-MM}/${project.name}-%d{yyyy-MM-dd}-%i.log.gz"}"
      PatternLayout:
        pattern: ${r"${log.pattern}"}
      Policies:
        TimeBasedTriggeringPolicy:  # 按天分类
          modulate: true
          interval: 1
      DefaultRolloverStrategy:     # 文件最多100个
        max: 100


  Loggers:
    Root:
      level: info
      AppenderRef:
      - ref: CONSOLE
      - ref: ROLLING_FILE
      - ref: EXCEPTION_ROLLING_FILE
    Logger:

    #平台日志
    - name: PLATFORM_ROLLING_FILE
      level: info
      additivity: false
      AppenderRef:
      - ref: CONSOLE
      - ref: PLATFORM_ROLLING_FILE

    # 业务日志
    - name: ${cfg.projectInfo.packagePath}.service
      level: info
      additivity: false
      AppenderRef:
      - ref: BUSSINESS_ROLLING_FILE
      - ref: CONSOLE

    # 异常日志
    - name: ${cfg.projectInfo.packagePath}
      level: info
      additivity: true
      AppenderRef:
      - ref: EXCEPTION_ROLLING_FILE

    - name: ${cfg.projectInfo.packagePath}.mapper
      level: info
      additivity: false
      AppenderRef:
      - ref: DB_ROLLING_FILE

    - name: ${cfg.projectInfo.packagePath}.mapper
      additivity: false
      level: ${r"${sys:log.level.mapper}"}
      AppenderRef:
      - ref: CONSOLE
      - ref: ROLLING_FILE