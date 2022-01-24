package top.zsmile.core.filter;

import top.zsmile.core.model.TablesModel;

import javax.swing.table.TableModel;
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

        List collect = list.stream().filter(item -> {

            String tableName = item.getTableName();

            // 忽略表名称
            List<String> ignoreTableName = filterConfig.getIgnoreTableName();
            boolean ignoreTableState = ignoreTableName != null ? ignoreTableName.contains(tableName) : false;

            // 忽略表前缀、后缀
            List<String> ignoreTablePrefix = filterConfig.getIgnoreTablePrefix();
            List<String> ignoreTableSuffix = filterConfig.getIgnoreTableSuffix();

            boolean ignoreTablePrefixState = ignoreTablePrefix != null ? ignoreTablePrefix.stream().anyMatch(prefix -> tableName.startsWith(prefix)) : false;
            boolean ignoreTableSuffixState = ignoreTableSuffix != null ? ignoreTableSuffix.stream().anyMatch(suffix -> tableName.startsWith(suffix)) : false;

            List<String> assignTableName = filterConfig.getAssignTableName();
            boolean assignTableNameState = assignTableName != null ? assignTableName.contains(tableName) : false;

            List<String> assignTablePrefix = filterConfig.getAssignTablePrefix();
            List<String> assignTableSuffix = filterConfig.getAssignTableSuffix();

            boolean assignTablePrefixState = assignTablePrefix != null ? assignTablePrefix.stream().anyMatch(prefix -> tableName.startsWith(prefix)) : false;
            boolean assignTableSuffixState = assignTableSuffix != null ? assignTableSuffix.stream().anyMatch(suffix -> tableName.startsWith(suffix)) : false;

            if (assignTableNameState || assignTablePrefixState || assignTableSuffixState) {
                return true;
            }

            if (ignoreTableState || ignoreTablePrefixState || ignoreTableSuffixState) {
                return false;
            }

            return false;
        }).collect(Collectors.toList());

        return collect;
    }


}
