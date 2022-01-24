package top.zsmile.core.handler.filter;

import top.zsmile.core.model.TablesModel;

import java.util.List;
import java.util.stream.Collectors;

public class ProcessFilter implements BaseFilter {

    private FilterConfig filterConfig;

    public ProcessFilter(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public List<TablesModel> filter(List<TablesModel> list) {
        if (list == null && list.size() == 0) {
            return list;
        }


        // 忽略表
        List<String> ignoreTableName = filterConfig.getIgnoreTableName();
        List<String> ignoreTablePrefix = filterConfig.getIgnoreTablePrefix();
        List<String> ignoreTableSuffix = filterConfig.getIgnoreTableSuffix();

        // 指定表
        List<String> assignTableName = filterConfig.getAssignTableName();
        List<String> assignTablePrefix = filterConfig.getAssignTablePrefix();
        List<String> assignTableSuffix = filterConfig.getAssignTableSuffix();


        List collect = list.stream().filter(item -> {

            String tableName = item.getTableName();

            boolean assignTableNameState = assignTableName != null ? assignTableName.contains(tableName) : false;
            boolean assignTablePrefixState = assignTablePrefix != null ? assignTablePrefix.stream().anyMatch(prefix -> tableName.startsWith(prefix)) : false;
            boolean assignTableSuffixState = assignTableSuffix != null ? assignTableSuffix.stream().anyMatch(suffix -> tableName.startsWith(suffix)) : false;

            boolean ignoreTableState = ignoreTableName != null ? ignoreTableName.contains(tableName) : false;
            boolean ignoreTablePrefixState = ignoreTablePrefix != null ? ignoreTablePrefix.stream().anyMatch(prefix -> tableName.startsWith(prefix)) : false;
            boolean ignoreTableSuffixState = ignoreTableSuffix != null ? ignoreTableSuffix.stream().anyMatch(suffix -> tableName.startsWith(suffix)) : false;

            if (checkList(assignTableName) || checkList(assignTablePrefix) || checkList(assignTableSuffix)) {
                if (assignTableNameState || assignTablePrefixState || assignTableSuffixState) {
                    return true;
                }
                if (ignoreTableState || ignoreTablePrefixState || ignoreTableSuffixState) {
                    return false;
                }
                return false;
            } else if (checkList(ignoreTableName) || checkList(ignoreTablePrefix) || checkList(ignoreTableSuffix)) {
                if (ignoreTableState || ignoreTablePrefixState || ignoreTableSuffixState) {
                    return false;
                }
                return true;
            } else {
                return true;
            }
        }).collect(Collectors.toList());

        return collect;
    }

    public boolean checkList(List list) {
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

}
