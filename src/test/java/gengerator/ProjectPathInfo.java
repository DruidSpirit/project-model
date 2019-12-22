package gengerator;

import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.regex.Matcher;

@Data
@Builder
public class ProjectPathInfo {

    /**
     * 模板名称
     */
    String moduleName;
    /**
     * 包路径
     */
    String packagePath;
    /**
     * 相对路径
     */
    String relativePath;

    public String getRelativePath() {

        if ( this.packagePath == null ) return null;

        return this.packagePath .replaceAll("\\.", Matcher.quoteReplacement("/")) + "/";
    }

    /**
     * 绝对路径
     */
    String absolutePath;

    public String getAbsolutePath() {

        if ( this.packagePath == null || this.moduleName == null ) return null;

        return System.getProperty("user.dir")  + "\\" + "src\\main\\java\\" + this.packagePath .replaceAll("\\.",Matcher.quoteReplacement(File.separator))+"\\";
    }

    /**
     * 配置源文件路径
     * @return
     */
    String resourcePath;

    public String getResourcePath() {

        if ( this.moduleName == null ) return null;

        return System.getProperty("user.dir") + "\\" + "src\\main\\resources\\";
    }

    /**
     * 测试路径
     */
    String testPath;

    public String getTestPath() {

        return ((System.getProperty("user.dir") + "/" + "src/test/java/")).replaceAll("\\\\","/");
    }
}
