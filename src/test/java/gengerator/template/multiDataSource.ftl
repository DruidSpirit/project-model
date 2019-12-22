package ${cfg.packageName}.common.config.bean;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Component
@ConfigurationProperties("multi-data-source")
public class MultiDataSource {
/**
* 数据源名称
*/
private String[] name;
/**
* 数据源连接地址
*/
private String[] url;
/**
* 连接用户
*/
private String[] username;
/**
* 连接密码
*/
private String[] password;
/**
* 连接驱动
*/
private String[] driverClassName;

/**
* 单个数据源模型
*/
private List<DataSourceBean> dataSourceBeanList;

    public List<DataSourceBean> getDataSourceBeanList() {
        if (
        this.name != null && this.name.length > 0
        &&this.name.length == this.driverClassName.length
        &&this.name.length == this.url.length
        &&this.name.length == this.username.length
        &&this.name.length == this.password.length
        ) {
        List<DataSourceBean> dataSourceBeanList = new LinkedList<>();
            int le = this.name.length;
            for (int i = 0; i < le; i++) {
            dataSourceBeanList.add(
            DataSourceBean.builder()
            .name(this.name[i])
            .url(this.url[i])
            .username(this.username[i])
            .password(this.password[i])
            .driverClassName(this.driverClassName[i])
            .build()
            );
            }
            return dataSourceBeanList;
            }
            throw  new RuntimeException("多数据源配置出错,请检查数据源的各项属性是否对应");
     }
}
