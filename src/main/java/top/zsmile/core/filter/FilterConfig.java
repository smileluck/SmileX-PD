package top.zsmile.core.filter;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FilterConfig {

//    public static FilterConfigBuilder builder(String key) {
//        return new FilterConfigBuilder().filterKey(key);
//    }
//
//    /**
//     * 过滤的字段名称
//     */
//    private String filterKey;

    /**
     * 忽略生成表名称
     */
    private List<String> ignoreTableName;
    /**
     * 忽略生成表前缀
     */
    private List<String> ignoreTablePrefix;
    /**
     * 忽略生成表后缀
     */
    private List<String> ignoreTableSuffix;
    /**
     * 指定生成表名称
     */
    private List<String> assignTableName;
    /**
     * 指定生成表前缀
     */
    private List<String> assignTablePrefix;
    /**
     * 指定生成表后缀
     */
    private List<String> assignTableSuffix;
}
