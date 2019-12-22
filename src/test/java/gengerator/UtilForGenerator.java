package gengerator;

import com.sun.deploy.util.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.yaml.snakeyaml.Yaml;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;
import java.util.function.Function;

/**
 * 代码自动生成工具类
 */
@SuppressWarnings("WeakerAccess")
@Log4j2
public class UtilForGenerator {


    //  读取yml文件数据源属性
    public static List<DataSourceBean> getYmlPropery(String url ) throws FileNotFoundException {
        String bootstrapUrl = url + "bootstrap.yml";
        String applicationUrl = url + "application-dev.yml";
        Map map = new HashMap();
        Yaml yaml = new Yaml();

        if (url != null) {

            try {
                map = yaml.load(new FileInputStream(bootstrapUrl));
            }catch ( FileNotFoundException e){
                map = yaml.load(new FileInputStream(applicationUrl));
            }

        }
        Map ma = (Map)map.get("multi-data-source");

        return MultiDataSource.builder()
                .name(StringTOArray(ma.get("name").toString()))
                .url(StringTOArray(ma.get("url").toString()))
                .driverClassName(StringTOArray(ma.get("driver-class-name").toString()))
                .username(StringTOArray(ma.get("username").toString()))
                .password(StringTOArray(ma.get("password").toString()))
                .build().getDataSourceBeanList();
    }

    /**
     * 将字符串改为数组
     * @param s 传入的字符串
     * @return 数组形式
     */
    private static String[] StringTOArray ( String s ) {
        s = s.replaceAll(" ","");
        return s.replace("[","").replace("]","").split(",");
    }

    /**
     * 根据模板文件创建Java文件
     * @param modelFilePath 模板文件路径
     * @param outJavaFilePath 输出Java文件路径
     * @param map 传入模板文件的参数
     * @return true 文件生成成功 false 文件生成失败
     */
    public static boolean createJavaFileByModel ( String modelFilePath, String outJavaFilePath, Map map ) {


        //  拆分并过滤模板文件路径
        modelFilePath = modelFilePath.replaceAll("\\\\","/");
        String mdriPah = modelFilePath.substring(0,modelFilePath.lastIndexOf("/"));
        String modelName = modelFilePath.substring(modelFilePath.lastIndexOf("/"));

        //  拆分并过滤输出文件路径
        outJavaFilePath = outJavaFilePath.replaceAll("\\\\","/");
        String outMdriPah = outJavaFilePath.substring(0,outJavaFilePath.lastIndexOf("/"));

        //  创建不存在的目录
        if (!new File(outMdriPah).isDirectory()) {
            new File(outMdriPah).mkdirs();
        }

        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
            cfg.setDirectoryForTemplateLoading(new File(mdriPah));
            cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_28));
            Template temp = cfg.getTemplate(modelName);
            File file = new File(outJavaFilePath);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            temp.process(map, bw);
            bw.flush();
            fw.close();
            return true;
        } catch (Exception e) {
            log.error(e);
        }
        return false;
    }

    /**
     * 读取微服务项目配置文件下各模板的路径
     * @return 模板路径对象列表
     */
    public static List<ProjectPathInfo> getProjectPathInfo() throws Exception {

        String basePath = System.getProperty("user.dir") + "\\";
        String readFileName = "pom.xml";

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbFactory.newDocumentBuilder();

        //将给定 URI 的内容解析为一个 XML 文档,并返回Document对象
        String parentPath = basePath + readFileName;
        Document document = db.parse( parentPath );

        //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList

        List<ProjectPathInfo> projectPathInfoArrayList = new ArrayList<>();

        //  获取父类pom.xml文件中各子模板的名称)
        Node name = document.getElementsByTagName("name").item(0);
        String moduleName = StringUtils.trimWhitespace(name.getTextContent());

        //  读取子模板的pom.mxl文件并提取包名
        String childPath = basePath  + "\\" + readFileName;
        document = db.parse( childPath );
        String groupId  = document.getElementsByTagName("groupId").item(1).getTextContent();
        String artifactId = document.getElementsByTagName("artifactId").item(1).getTextContent();
        String packagePath = groupId + "." + artifactId.replaceAll("-","");

        projectPathInfoArrayList.add(
                ProjectPathInfo
                        .builder()
                        .moduleName( moduleName )
                        .packagePath( packagePath )
                        .build()
        );

        return projectPathInfoArrayList;
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。
     * 例如：HELLO_WORLD->HelloWorld
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String camelName(String name,boolean isHeadtoLower) {

        name = name.replaceAll("-","_");
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (Objects.isNull(name)||name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写或大写
            if ( isHeadtoLower ) {
                return name.substring(0, 1).toLowerCase() + name.substring(1);
            }
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }

        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel :  camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写或大写
                    result.append(camel.toLowerCase());

            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }

        //  最后选择首字母是大写还是小写
        if ( isHeadtoLower ) {
            return result.toString().substring(0, 1).toLowerCase() + result.toString().substring(1);
        }
        return result.toString().substring(0, 1).toUpperCase() + result.toString().substring(1);

    }

    /**
     * 修改文件夹名称
     * @param oldPathName 老文件夹名名称及路径
     * @param newPathName  新的文件夹名称及路径
     * @return true 修改成功 false 修改失败
     */
    public static boolean reNameFolder ( String oldPathName, String newPathName ) {

        return new File(oldPathName).renameTo(new File(newPathName));
    }

    /**
     * 将集合分解成根据相应函数形式代码的字符串样式
     * @param list 被分解的集合
     * @param function 函数操作
     * @param <T> 集合类型对象
     * @return 函数操作形成的结果字符串
     */
    public static<T> String getIndexWithNameString ( List<T> list, Function<T,String> function ) {

        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (T t : list) {
            stringBuilder.append(i).append("-").append(function.apply(t)).append("    ");
            i++;
        }
        return stringBuilder.toString();
    }

}
