package ${cfg.packageName}.enums;

import ${cfg.packageName}.common.model.ResponseInterface;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ResponseDataEnums implements ResponseInterface {

    RESPONSE_SUCCESS(2000,"数据响应成功"),

    RESPONSE_FAIL_PARAMS(3000,"参数响应失败"),

    RESPONSE_FAIL(5000,"数据响应失败"),

;

    private Integer statusCode;
    private String name;

    @Override
    public Integer getStatusCode() {
    return this.statusCode;
    }

    @Override
    public String getName() {
    return this.name;
    }
}
