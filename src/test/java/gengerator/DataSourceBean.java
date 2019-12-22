package gengerator;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DataSourceBean {
    /**
     * 数据源名称
     */
    private String name;
    /**
     * 数据源名称驼峰命名
     */
    private String camelName;

    public String getCamelName() {

        if ( this.name == null ) return null;
        return StringUtils.underlineToCamel( this.name.replaceAll("-","_").trim() );
    }

    /**
     * 数据源连接地址
     */
    private String url;
    /**
     * 连接用户
     */
    private String username;
    /**
     * 连接密码
     */
    private String password;
    /**
     * 连接驱动
     */
    private String driverClassName;
    /**
     * 数据源作用包路径
     */
    private String packageMapperPath;
    /**
     * 数据源service作用包路径
     */
    private String packageServicePath;
}
