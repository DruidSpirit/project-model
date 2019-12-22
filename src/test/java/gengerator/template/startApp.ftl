package ${cfg.packageName};

import com.spring4all.swagger.EnableSwagger2Doc;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableSwagger2Doc
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan(basePackages = "${cfg.packageName}.mapper.*")
@SpringBootApplication
public class ${cfg.projectClassName} {

    public static void main(String[] args) {
        SpringApplication.run(${cfg.projectClassName}.class, args);
    }

}
