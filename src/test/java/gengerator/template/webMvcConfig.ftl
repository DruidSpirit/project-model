package ${cfg.packageName}.common.config;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Data
@Log4j2
@Configuration
@Component
@ConfigurationProperties("web-mvc-config")
public class WebMvcConfig implements WebMvcConfigurer {

    /**
    * 跨域设置
    */
    private String[] origins;

    /**
    * 跨域设置
    */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        boolean allowCredentials = true;
        if ( origins == null || origins.length == 0) {
        allowCredentials = false;
        origins = new String[]{"*"};
        }
        registry.addMapping("/**")
        .allowedOrigins(origins)
        .allowedMethods("PUT", "POST", "GET", "OPTIONS", "DELETE")
        .allowedHeaders("Content-Type", "Access-Token")
        .exposedHeaders("**")
        .allowCredentials(allowCredentials).maxAge(3600);
    }

}
