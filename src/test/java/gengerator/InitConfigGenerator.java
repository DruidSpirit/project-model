package gengerator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.extern.log4j.Log4j2;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 初始化项目文件路径
 */
@SuppressWarnings("unchecked")
@Log4j2
public class InitConfigGenerator {

    public static void main(String[] args) throws Exception {

        //  获取maven项目信息
        List<ProjectPathInfo> projectPathInfoList = UtilForGenerator.getProjectPathInfo();
        ProjectPathInfo projectPathInfo = projectPathInfoList.get(0);

        //  读取数据源信息
        List<DataSourceBean> dataSourceBeanList = UtilForGenerator.getYmlPropery( projectPathInfo.getResourcePath() );
        dataSourceBeanList.stream()
                .peek(dataSourceBean ->
                        dataSourceBean
                                .setPackageMapperPath(projectPathInfo.getPackagePath()+".mapper."+dataSourceBean.getName().replaceAll("-","").trim()+"mapper")
                                .setPackageServicePath(projectPathInfo.getPackagePath()+".service."+dataSourceBean.getName().replaceAll("-","").trim()+"service")
                ).collect(Collectors.toList());

        //  更改项目包文件名称
       // UtilForGenerator.reNameProjectPackage("com/prmsce/management/prmscemanagement",projectPathInfo.relativePath);

     // 模板引擎的路径
        String templatePathDB = projectPathInfo.getTestPath() + "gengerator/template/dataSourceSwitchAspect.java.ftl";    //  生成动态数据源切换配置
        String templatePathLog = projectPathInfo.getTestPath() + "gengerator/template/log4j2.yml.ftl";                    //  生成日志文件
        String templateStartApp =  projectPathInfo.getTestPath() + "gengerator/template/startApp.ftl";                    //  重新启动文件配置
        String templateDataSourceBean =  projectPathInfo.getTestPath() + "gengerator/template/dataSourceBean.ftl";        //  生成数据源bean模型
        String templateDbContextHolder =  projectPathInfo.getTestPath() + "gengerator/template/dbContextHolder.ftl";      //  生成动态数据源上下文配置类
        String templatedDynamicDataSource =  projectPathInfo.getTestPath() + "gengerator/template/dynamicDataSource.ftl"; //  生成动态数据源配置类
        String templateMultiDataSource =  projectPathInfo.getTestPath() + "gengerator/template/multiDataSource.ftl";      //  生成多数据源bean模型
        String templateMybatisPlusConfig =  projectPathInfo.getTestPath() + "gengerator/template/mybatisPlusConfig.ftl";  //  生成mybatis-plus配置类
        String templateWebLogAspect =  projectPathInfo.getTestPath() + "gengerator/template/webLogAspect.ftl";            //  生成aop日志打印类
        String templateWebMvcConfig =  projectPathInfo.getTestPath() + "gengerator/template/webMvcConfig.ftl";            //  生成webmvc配置类
        String templatePageBody =  projectPathInfo.getTestPath() + "gengerator/template/pageBody.ftl";                    //  生成分页处理加工类
        String templateResponseData =  projectPathInfo.getTestPath() + "gengerator/template/responseData.ftl";            //  生成数据响应类
        String templateResponseInterface =  projectPathInfo.getTestPath() + "gengerator/template/responseInterface.ftl";  //  生成数据响应接口
        String templateResponseDataEnums =  projectPathInfo.getTestPath() + "gengerator/template/responseDataEnums.ftl";  //  生成数据响应枚举

        // 写出的Java文件路径
        String outPathDB = projectPathInfo.getAbsolutePath() + "/common/aspect/" + "DataSourceSwitchAspect" + StringPool.DOT_JAVA;
        String outPathLogDev = projectPathInfo.getResourcePath() + "logging/log4j2-dev.yml";
        String outPathLogTest = projectPathInfo.getResourcePath() + "logging/log4j2-test.yml";
        String outPathLogProd = projectPathInfo.getResourcePath() + "logging/log4j2-prod.yml";
        String appName = UtilForGenerator.camelName( projectPathInfo.getModuleName(),false ) + "Application";
        String startApp = projectPathInfo.getAbsolutePath() + "/" +appName + StringPool.DOT_JAVA;
        String outPathDataSourceBean = projectPathInfo.getAbsolutePath() + "/common/config/bean/" + "DataSourceBean" + StringPool.DOT_JAVA;
        String outPathDbContextHolder = projectPathInfo.getAbsolutePath() + "/common/config/DataSourceConfig/" + "DbContextHolder" + StringPool.DOT_JAVA;
        String outPathMultiDataSource = projectPathInfo.getAbsolutePath() + "/common/config/bean/" + "MultiDataSource" + StringPool.DOT_JAVA;
        String outPathDynamicDataSource = projectPathInfo.getAbsolutePath() + "/common/config/DataSourceConfig/" + "DynamicDataSource" + StringPool.DOT_JAVA;
        String outPathMybatisPlusConfig = projectPathInfo.getAbsolutePath() + "/common/" + "MybatisPlusConfig" + StringPool.DOT_JAVA;
        String outPathWebLogAspect = projectPathInfo.getAbsolutePath() + "/common/aspect/" + "WebLogAspect" + StringPool.DOT_JAVA;
        String outPathWebMvcConfig = projectPathInfo.getAbsolutePath() + "/common/config/" + "WebMvcConfig" + StringPool.DOT_JAVA;
        String outPathPageBody = projectPathInfo.getAbsolutePath() + "/common/model/" + "PageBody" + StringPool.DOT_JAVA;
        String outPathResponseData = projectPathInfo.getAbsolutePath() + "/common/model/" + "ResponseData" + StringPool.DOT_JAVA;
        String outPathResponseInterface = projectPathInfo.getAbsolutePath() + "/common/model/" + "ResponseInterface" + StringPool.DOT_JAVA;
        String outPathResponseDataEnums = projectPathInfo.getAbsolutePath() + "/enums/" + "ResponseDataEnums" + StringPool.DOT_JAVA;

        //  将需要处理文件文件统一放到map集合中
        Map<String,String> fileMap = new HashMap();
        fileMap.put( outPathDB, templatePathDB );
        fileMap.put( outPathLogDev, templatePathLog );
        fileMap.put( outPathLogTest, templatePathLog );
        fileMap.put( outPathLogProd, templatePathLog );
        fileMap.put( startApp, templateStartApp );
        fileMap.put( outPathDataSourceBean, templateDataSourceBean );
        fileMap.put( outPathDbContextHolder, templateDbContextHolder );
        fileMap.put( outPathMultiDataSource, templateMultiDataSource );
        fileMap.put( outPathDynamicDataSource, templatedDynamicDataSource );
        fileMap.put( outPathMybatisPlusConfig, templateMybatisPlusConfig );
        fileMap.put( outPathWebLogAspect, templateWebLogAspect );
        fileMap.put( outPathWebMvcConfig, templateWebMvcConfig );
        fileMap.put( outPathPageBody, templatePageBody );
        fileMap.put( outPathResponseData, templateResponseData );
        fileMap.put( outPathResponseInterface, templateResponseInterface );
        fileMap.put( outPathResponseDataEnums, templateResponseDataEnums );

        //组织需要输入到模板文件中的参数
        Map paramMap = new HashMap(1);
        Map unifyParam = new HashMap(); //  为了统一mybatis-plus的自动生成工具的参数，这里加了个容器用cfg作为键来装
        unifyParam.put("dbs",dataSourceBeanList);
        unifyParam.put("projectInfo",projectPathInfo);
        unifyParam.put("packageName",projectPathInfo.getPackagePath());
        unifyParam.put("projectClassName",appName);
        unifyParam.put("UUID", UUID.randomUUID().getMostSignificantBits()+"L");
        paramMap.put("cfg",unifyParam);
        paramMap.put("author","druid-elf");
        paramMap.put("date", LocalDateTime.now().toString());

        //  开始生成Java类文件
        for (String outJavaFilePath : fileMap.keySet()) {

            String modelFilePath = fileMap.get(outJavaFilePath);
            if ( !UtilForGenerator.createJavaFileByModel(modelFilePath,outJavaFilePath,paramMap) ) {
                log.error(outJavaFilePath+"文件生成失败!");
                continue;
            }
            System.out.println(outJavaFilePath+"文件生成成功!");
        }
    }

}
