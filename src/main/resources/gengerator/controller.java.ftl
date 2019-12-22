package ${package.Controller};

<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import ${cfg.packageName}.common.model.PageBody;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${cfg.packageName}.common.model.ResponseData;
import ${package.Entity}.${entity};
<#if swagger2>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
</#if>
import ${package.Service}.${table.serviceName};
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>

/**
 * @author ${author}
 * @since ${date}
 */
<#if swagger2>
@Api(tags={"${table.comment!}前端控制器"})
</#if>
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
@RequiredArgsConstructor
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {

</#if>
    private final ${table.serviceName} ${table.serviceName};

    @ApiOperation("得到${table.comment!}分页列表")
    @GetMapping(value = "get${entity}List")
    public ResponseData<PageBody<${entity}>>get${entity}List() {

        return ResponseData.SUCCESS(
            PageBody.dealWithIPage( ${table.serviceName}.page(new Page()) )
        );
    }
}
</#if>
