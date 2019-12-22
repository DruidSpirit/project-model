package ${cfg.packageName}.common.aspect;

import ${cfg.packageName}.common.config.DataSourceConfig.DbContextHolder;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
*
* 多数据源动态切换
*
* @author ${author}
* @since ${date}
*/
@Component
@Order(value = -100)
@Log4j2
@Aspect
public class DataSourceSwitchAspect {

    <#list cfg.dbs as db>
    // ${db.camelName}数据源
    private static final String ${db.camelName}Mapper = "${db.packageMapperPath}";
    private static final String ${db.camelName}Service = "${db.packageServicePath}";
    private static final String ${db.camelName}Execution = "execution(* "+${db.camelName}Mapper+"..*.*(..))||execution(* "+${db.camelName}Service+"..*.*(..))";
    </#list>

    <#list cfg.dbs as db>
    @Before(${db.camelName}Execution)
    public void ${db.camelName}() { common("${db.name}"); }
    </#list>

    private void common ( String name ) {
        log.info("切换到数据源..."+ name );
        DbContextHolder.setDbType( name );
    }

}
