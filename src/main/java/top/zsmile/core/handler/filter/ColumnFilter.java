package top.zsmile.core.handler.filter;

import top.zsmile.core.model.ColumnsModel;
import top.zsmile.core.model.TablesModel;

import java.util.List;
import java.util.stream.Collectors;

public class ColumnFilter implements BaseFilter<ColumnsModel> {
    private ColumnFilterConfig columnFilterConfig;

    public ColumnFilter(ColumnFilterConfig columnFilterConfig) {
        this.columnFilterConfig = columnFilterConfig;
    }

    @Override
    public List<ColumnsModel> filter(List<ColumnsModel> list) {


        if (list == null && list.size() == 0) {
            return list;
        }
        // 忽略表
        List<String> ignoreName = columnFilterConfig.getIgnoreName();
        List<String> ignorePrefix = columnFilterConfig.getIgnorePrefix();
        List<String> ignoreSuffix = columnFilterConfig.getIgnoreSuffix();
        List<String> ignoreComment = columnFilterConfig.getIgnoreComment();

        // 指定表
        List<String> assignName = columnFilterConfig.getAssignName();
        List<String> assignPrefix = columnFilterConfig.getAssignPrefix();
        List<String> assignSuffix = columnFilterConfig.getAssignSuffix();
        List<String> assignComment = columnFilterConfig.getAssignComment();


        List collect = list.stream().filter(item -> {

            String columnName = item.getColumnName();


            boolean assignNameState = assignName != null ? assignName.contains(columnName) : false;
            boolean assignPrefixState = assignPrefix != null ? assignPrefix.stream().anyMatch(prefix -> columnName.startsWith(prefix)) : false;
            boolean assignSuffixState = assignSuffix != null ? assignSuffix.stream().anyMatch(suffix -> columnName.startsWith(suffix)) : false;
            boolean assignCommentState = assignComment != null ? assignComment.stream().anyMatch(comment -> columnName.contains(comment)) : false;

            boolean ignoreState = ignoreName != null ? ignoreName.contains(columnName) : false;
            boolean ignorePrefixState = ignorePrefix != null ? ignorePrefix.stream().anyMatch(prefix -> columnName.startsWith(prefix)) : false;
            boolean ignoreSuffixState = ignoreSuffix != null ? ignoreSuffix.stream().anyMatch(suffix -> columnName.startsWith(suffix)) : false;
            boolean ignoreCommentState = ignoreComment != null ? ignoreComment.stream().anyMatch(comment -> columnName.contains(comment)) : false;

            if (columnFilterConfig.checkList(assignName) || columnFilterConfig.checkList(assignPrefix) || columnFilterConfig.checkList(assignSuffix) || columnFilterConfig.checkList(assignComment)) {
                if (assignNameState || assignPrefixState || assignSuffixState || assignCommentState) {
                    return true;
                }
                if (ignoreState || ignorePrefixState || ignoreSuffixState || ignoreCommentState) {
                    return false;
                }
                return false;
            } else if (columnFilterConfig.checkList(ignoreName) || columnFilterConfig.checkList(ignorePrefix) || columnFilterConfig.checkList(ignoreSuffix) || columnFilterConfig.checkList(ignoreComment)) {
                if (ignoreState || ignorePrefixState || ignoreSuffixState || ignoreCommentState) {
                    return false;
                }
                return true;
            } else {
                return true;
            }
        }).collect(Collectors.toList());

        return collect;
    }
}
