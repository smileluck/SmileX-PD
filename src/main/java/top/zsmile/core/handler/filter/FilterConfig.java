package top.zsmile.core.handler.filter;

import lombok.Data;

import java.util.List;

@Data
public abstract class FilterConfig {
    /**
     * 忽略字段名名称
     */
    protected List<String> ignoreName;
    /**
     * 忽略字段名前缀
     */
    protected List<String> ignorePrefix;
    /**
     * 忽略字段名后缀
     */
    protected List<String> ignoreSuffix;
    /**
     * 忽略字段名注释
     */
    protected List<String> ignoreComment;
    /**
     * 指定字段名名称
     */
    protected List<String> assignName;
    /**
     * 指定字段名前缀
     */
    protected List<String> assignPrefix;
    /**
     * 指定字段名后缀
     */
    protected List<String> assignSuffix;
    /**
     * 忽略字段名注释
     */
    protected List<String> assignComment;

    public boolean checkList(List list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }
}
