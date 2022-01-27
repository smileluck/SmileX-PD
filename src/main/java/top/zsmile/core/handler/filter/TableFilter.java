package top.zsmile.core.handler.filter;

import top.zsmile.core.model.TablesModel;

import java.util.List;
import java.util.stream.Collectors;

public class TableFilter implements BaseFilter<TablesModel> {

    private TableFilterConfig tableFilterConfig;

    public TableFilter(TableFilterConfig tableFilterConfig) {
        this.tableFilterConfig = tableFilterConfig;
    }

    @Override
    public List<TablesModel> filter(List<TablesModel> list) {
        if (list == null && list.size() == 0) {
            return list;
        }
        // 忽略表
        List<String> ignoreName = tableFilterConfig.getIgnoreName();
        List<String> ignorePrefix = tableFilterConfig.getIgnorePrefix();
        List<String> ignoreSuffix = tableFilterConfig.getIgnoreSuffix();
        List<String> ignoreComment = tableFilterConfig.getIgnoreComment();

        // 指定表
        List<String> assignName = tableFilterConfig.getAssignName();
        List<String> assignPrefix = tableFilterConfig.getAssignPrefix();
        List<String> assignSuffix = tableFilterConfig.getAssignSuffix();
        List<String> assignComment = tableFilterConfig.getAssignComment();


        List collect = list.stream().filter(item -> {

            String tableName = item.getTableName();
            boolean assignNameState = assignName != null ? assignName.contains(tableName) : false;
            boolean assignPrefixState = assignPrefix != null ? assignPrefix.stream().anyMatch(prefix -> tableName.startsWith(prefix)) : false;
            boolean assignSuffixState = assignSuffix != null ? assignSuffix.stream().anyMatch(suffix -> tableName.startsWith(suffix)) : false;
            boolean assignCommentState = assignComment != null ? assignComment.stream().anyMatch(comment -> tableName.contains(comment)) : false;

            boolean ignoreState = ignoreName != null ? ignoreName.contains(tableName) : false;
            boolean ignorePrefixState = ignorePrefix != null ? ignorePrefix.stream().anyMatch(prefix -> tableName.startsWith(prefix)) : false;
            boolean ignoreSuffixState = ignoreSuffix != null ? ignoreSuffix.stream().anyMatch(suffix -> tableName.startsWith(suffix)) : false;
            boolean ignoreCommentState = ignoreComment != null ? ignoreComment.stream().anyMatch(comment -> tableName.contains(comment)) : false;

            if (tableFilterConfig.checkList(assignName) || tableFilterConfig.checkList(assignPrefix) || tableFilterConfig.checkList(assignSuffix) || tableFilterConfig.checkList(assignComment)) {
                if (assignNameState || assignPrefixState || assignSuffixState || assignCommentState) {
                    return true;
                }
                if (ignoreState || ignorePrefixState || ignoreSuffixState || ignoreCommentState) {
                    return false;
                }
                return false;
            } else if (tableFilterConfig.checkList(ignoreName) || tableFilterConfig.checkList(ignorePrefix) || tableFilterConfig.checkList(ignoreSuffix) || tableFilterConfig.checkList(ignoreComment)) {
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
