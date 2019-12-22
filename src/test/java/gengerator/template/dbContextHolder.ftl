package ${cfg.packageName}.common.config.DataSourceConfig;

@SuppressWarnings({"unused", "WeakerAccess", "unchecked"})
public class DbContextHolder {

    private static final ThreadLocal contextHolder = new ThreadLocal<>();
    /**
    * 设置数据源
    * @param name 数据源名称
    */
    public static void setDbType(String name) {
        contextHolder.set(name);
    }

    /**
    * 取得当前数据源
    * @return 返回当前数据源名称
    */
    public static String getDbType() {
        return (String) contextHolder.get();
    }

    /**
    * 清除上下文数据
    */
    public static void clearDbType() {
        contextHolder.remove();
    }
}
