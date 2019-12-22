package ${cfg.packageName}.common.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"JavaDoc", "WeakerAccess"})
@Data
@ApiModel(value = "分页返回体")
@NoArgsConstructor
public class PageBody<T> implements Serializable {

    private static final long serialVersionUID = ${cfg.UUID};
    private static final int DEFAULT_PAGE_NUM_SIZE = 8;

    @ApiModelProperty( "分页总条数" )
    private long total;

    @ApiModelProperty( "当前页" )
    private long pageNum;

    @ApiModelProperty( "每页条数" )
    private long pageSize;

    @ApiModelProperty( "总页数" )
    private long pageTotal;

    @ApiModelProperty( "分页条的长度" )
    private Integer pageNumSize;

    @ApiModelProperty( "分页条的页码" )
    private List<Long> pageNums;

    @ApiModelProperty( "响应结果" )
    private List<T> list;

    /**
    * 初始化并计算分页相关参数
    */
    public PageBody(long total, long pageNum, long pageSize, long pageTotal, List<T> list, Integer pageNumSize ) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pageTotal = pageTotal;
        this.list = list;
        if ( pageNumSize == null ) pageNumSize = DEFAULT_PAGE_NUM_SIZE;
        this.pageNumSize = pageNumSize;

        //  计算当前页所在的页码范围
        long d = this.pageNum/this.pageNumSize;    //  获取当前页d段页码上面
        if ( this.pageNum%this.pageNumSize == 0 ) d = d-1;
        this.pageNums = new ArrayList<>();
        for ( long i = d*this.pageNumSize+1; i < d*this.pageNumSize + this.pageNumSize+1; i++ ) {
        if ( i>this.pageTotal ) break;
        this.pageNums.add(i);
        }

    }

     /**
     * 提取mybatis的分页数据重新包装
     * @param page mybatis-plus分页对象
     * @param <T> 分页的实体泛型
     * @return 本类实例
     */
     public static <T>PageBody<T> dealWithIPage(IPage<T> page){

     return new PageBody<>( page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), page.getRecords(),DEFAULT_PAGE_NUM_SIZE );
     }

     /**
     * 提取mybatis的分页数据重新包装
     * @param page mybatis-plus分页对象
     * @param pageNumSize 每页尺寸大小
     * @param <T> 分页的实体泛型
     * @return 本类实例
     */
     public static <T>PageBody<T> dealWithIPage( IPage<T> page, Integer pageNumSize ){

         return new PageBody<>( page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), page.getRecords(),pageNumSize );
     }

     /**
     * 将list集合包装成分页数据,同时使用替代的list来作为其返回数据
     * @param page mybatis-plus分页对象
     * @param replaceList 分页后需要被替换的分页列表数据
     * @param <T> 分页的实体泛型
     * @return 本类实例
     */
     public static <T>PageBody<T> dealWithList( IPage<?> page , List<T> replaceList ){

         return new PageBody<>( page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), replaceList,DEFAULT_PAGE_NUM_SIZE);
     }

     /**
     * 将list集合包装成分页数据,同时使用替代的list来作为其返回数据
     * @param page mybatis-plus分页对象
     * @param replaceList 分页后需要被替换的分页列表数据
     * @param pageNumSize 每页尺寸大小
     * @param <T> 分页的实体泛型
     * @return 本类实例
     */
     public static <T>PageBody<T> dealWithList( IPage<?> page , List<T> replaceList, Integer pageNumSize ){

         return new PageBody<>( page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), replaceList, pageNumSize );
     }
}