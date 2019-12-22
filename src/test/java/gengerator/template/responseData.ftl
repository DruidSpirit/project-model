package ${cfg.packageName}.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import static ${cfg.packageName}.enums.ResponseDataEnums.RESPONSE_FAIL_PARAMS;
import static ${cfg.packageName}.enums.ResponseDataEnums.RESPONSE_SUCCESS;

@SuppressWarnings({"unused", "JavaDoc", "unchecked"})
@Data
@ApiModel(value = "数据返回体")
@NoArgsConstructor
public class ResponseData<E> implements Serializable{

    private static final long serialVersionUID = ${cfg.UUID};

    @ApiModelProperty( "响应结果" )
    private boolean status;

    @ApiModelProperty( "响应状态码" )
    private Integer statusCode;

    @ApiModelProperty( "响应提示语" )
    private String message;

    @ApiModelProperty( "响应数据" )
    private E data;

    private ResponseData(ResponseInterface responseInterface ) {
    this.statusCode = responseInterface.getStatusCode();
    this.message = responseInterface.getName();
    }

    /**
    * 数据响应成功
    * @param  data
    * @return responseBody
    */
    public static <E>ResponseData<E> SUCCESS ( E data ) {
        ResponseData<E> responseBody = new ResponseData( RESPONSE_SUCCESS );
        responseBody.setStatus(true);
        responseBody.setData(data);
    return responseBody;
    }

    /**
    * 请求参数响应失败
    * @param data
    * @return responseBody
    */
    public static <E>ResponseData<E>  FAILURE ( E data ) {
        ResponseData<E> responseBody = new ResponseData( RESPONSE_FAIL_PARAMS );
        responseBody.setStatus(false);
        responseBody.setData(data);
    return responseBody;
    }

    /**
    * 操作响应成功
    * @param responseInterface
    * @return responseBody
    */
    public static ResponseData SUCCESS ( ResponseInterface responseInterface ) {
        ResponseData responseBody = new ResponseData(responseInterface);
        responseBody.setStatus(true);
    return responseBody;
    }

    /**
    * 操作响应失败
    * @param responseInterface
    * @return responseInterface
    */
    public static ResponseData FAILURE (  ResponseInterface responseInterface  ) {
        ResponseData responseBody = new ResponseData(responseInterface);
        responseBody.setStatus(false);
    return responseBody;
    }
}