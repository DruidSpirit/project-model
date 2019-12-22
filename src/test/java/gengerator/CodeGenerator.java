package gengerator;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.extern.log4j.Log4j2;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(("请输入" + tip + "："));
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) throws Exception {

        List<ProjectPathInfo> projectPathInfoList = UtilForGenerator.getProjectPathInfo();
        ProjectPathInfo projectPathInfo = projectPathInfoList.get(0);

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir") + "\\";
        String projectCommonPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("druid-elf");
        gc.setOpen(false);
        gc.setSwagger2(true);// 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();

        List<DataSourceBean> dataSourceBeanList = UtilForGenerator.getYmlPropery( projectPathInfo.getResourcePath() );
        //  设置数据源模块包路径
        dataSourceBeanList.stream()
                .peek(dataSourceBean ->
                        dataSourceBean
                                .setPackageMapperPath(projectPathInfo.getPackagePath()+".mapper."+dataSourceBean.getName().replaceAll("-","").trim()+"mapper")
                                .setPackageServicePath(projectPathInfo.getPackagePath()+".service."+dataSourceBean.getName().replaceAll("-","").trim()+"service")
                ).collect(Collectors.toList());

        int index = 0;
        if ( dataSourceBeanList.size() > 1) {
            index = Integer.parseInt(scanner("请选择数据源:\n"+ UtilForGenerator.getIndexWithNameString(dataSourceBeanList,DataSourceBean::getName)));
        }

        DataSourceBean dataSourceBean = dataSourceBeanList.get(index);
        dsc.setUrl(dataSourceBean.getUrl().trim());
        // dsc.setSchemaName("public");
        dsc.setDriverName(dataSourceBean.getDriverClassName().trim());
        dsc.setUsername(dataSourceBean.getUsername().trim());
        dsc.setPassword(dataSourceBean.getPassword().trim());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
       // pc.setModuleName(projectPathInfo.getModuleName());

        pc.setParent(projectPathInfo.getPackagePath());
        pc.setMapper(dataSourceBean.getPackageMapperPath().replace(pc.getParent()+".","").trim());
        pc.setService(dataSourceBean.getPackageServicePath().replace(pc.getParent()+".","").trim());
        pc.setEntity((projectPathInfo.packagePath +".entity").replace(pc.getParent()+".","").trim());
        Map<String, String> pathInfo = new HashMap<>();
        pathInfo.put("entity_path",projectPathInfo.getAbsolutePath() + pc.getEntity().replaceAll("\\.","/"));
        pathInfo.put("mapper_path",projectPathInfo.getAbsolutePath() + pc.getMapper().replaceAll("\\.","/"));
        pathInfo.put("service_path",projectPathInfo.getAbsolutePath() + pc.getService().replaceAll("\\.","/"));
        pc.setPathInfo(pathInfo);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                map.put("UUID", UUID.randomUUID().getMostSignificantBits()+"L");
                map.put("packageName",pc.getParent());
                map.put("dbs",dataSourceBeanList);
                map.put("projectInfo",projectPathInfo);
                this.setMap(map);
            }
        };

        // 如果模板引擎是 freemarker
        String templatePath = "templates/mapper.xml.ftl";
        String templatePathEntity = "gengerator/entity.java.ftl";
        String templatePathController = "gengerator/controller.java.ftl";
        String templatePathserviceImpl = "gengerator/serviceImpl.java.ftl";
        String templatePathservice = "gengerator/service.java.ftl";
        String templatePathmapper = "gengerator/mapper.java.ftl";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/"
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        focList.add(new FileOutConfig(templatePathEntity) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectCommonPath + "/src/main/java/"+projectPathInfo.getRelativePath()+"entity"
                        + "/" + tableInfo.getEntityName() + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig(templatePathController) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/"+projectPathInfo.getRelativePath()+"controller"
                        + "/" + tableInfo.getControllerName() + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig(templatePathserviceImpl) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/"+projectPathInfo.getRelativePath()+"service/impl"
                        + "/" + tableInfo.getServiceImplName() + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig(templatePathservice) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/"+projectPathInfo.getRelativePath()+pc.getService().replaceAll("\\.","/")
                        + "/" + tableInfo.getServiceName() + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig(templatePathmapper) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/java/"+projectPathInfo.getRelativePath()+pc.getMapper().replaceAll("\\.","/")
                        + "/" + tableInfo.getMapperName() + StringPool.DOT_JAVA;
            }
        });

        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);


        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);

        strategy.setEntityBuilderModel(false);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));

        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);

        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}
