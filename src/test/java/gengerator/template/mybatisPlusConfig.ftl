package ${cfg.packageName}.common;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import ${cfg.packageName}.common.config.DataSourceConfig.DynamicDataSource;
import ${cfg.packageName}.common.config.bean.MultiDataSource;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@EnableTransactionManagement
@Configuration
@Log4j2
public class MybatisPlusConfig {

    @Autowired
    MultiDataSource multiDataSource;
    /**
    * 分页插件配置
    */
    @Bean
    public PaginationInterceptor paginationInterceptor() {

        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setLimit(100000);
        return paginationInterceptor;
    }

    @Bean(name = "dataSourceMap")
    public Map<String,DataSource> dataSourceMap () {
        Map<String,DataSource> dataSourceMap = new HashMap<>();
        multiDataSource.getDataSourceBeanList().forEach(dataSourceBean -> {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dataSourceBean.getDriverClassName());
        dataSource.setUrl(dataSourceBean.getUrl());
        dataSource.setUsername(dataSourceBean.getUsername());
        dataSource.setPassword(dataSourceBean.getPassword());
        dataSourceMap.put(dataSourceBean.getName(),dataSource);
        });
        return dataSourceMap;
    }
    /**
    * 动态数据源配置
    * @return 数据源对象
    */
    @Bean
    public DataSource multipleDataSource( @Qualifier("dataSourceMap") Map<String,DataSource> dataSourceMap ) {

        System.out.println("\033[47;4m" + "开始配置动态数据源" + "\033[0m");
        System.out.println(
        "  .--,       .--,\n" +
        " ( (  \\.---./  ) )\n" +
        "  '.__/o   o\\__.'\n" +
        "     {=  ^  =}\n" +
        "      >  -  <\n" +
        "     /       \\\n" +
        "    //       \\\\\n" +
        "   //|   .   |\\\\\n" +
        "   \"'\\       /'\"_.-~^`'-.\n" +
        "      \\  _  /--'         `\n" +
        "    ___)( )(___\n" +
        "   (((__) (__)))    梦想是个奇怪的东西,像是勇敢的挑战,更像是天真的逃避的方式。\n"
        );

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        dataSourceMap.forEach((s, dataSource) -> {
        targetDataSources.put(s, dataSource);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        });

        dynamicDataSource.setDefaultTargetDataSource(
        dataSourceMap.get( multiDataSource.getDataSourceBeanList().get(0).getName() )
        );
        return dynamicDataSource;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(multipleDataSource(dataSourceMap ()));

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        //PerformanceInterceptor(),OptimisticLockerInterceptor()
        //添加分页功能
        sqlSessionFactory.setPlugins(new Interceptor[]{
        paginationInterceptor()
        });
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        //        sqlSessionFactory.setGlobalConfig(globalConfiguration());
        return sqlSessionFactory.getObject();
    }

    @Bean(name = "dynamicTransactionManager")
    @Primary
    public DataSourceTransactionManager mainTransactionManager(@Qualifier("multipleDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}