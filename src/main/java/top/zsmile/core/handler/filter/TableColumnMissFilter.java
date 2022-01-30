package top.zsmile.core.handler.filter;

import org.apache.logging.log4j.util.Strings;
import top.zsmile.core.model.ColumnsModel;
import top.zsmile.core.model.TablesModel;

import java.util.ArrayList;
import java.util.List;

public class TableColumnMissFilter implements BaseFilter<TablesModel> {

    private ColumnMissConfig columnMissConfig;

    public TableColumnMissFilter(ColumnMissConfig columnMissConfig) {
        this.columnMissConfig = columnMissConfig;
    }


    @Override
    public List<TablesModel> filter(List<TablesModel> list) {
        for (TablesModel tablesModel : list) {
            List<ColumnsModel> columnsModelList = tablesModel.getColumnsModelList();
            if (columnsModelList == null || columnsModelList.size() == 0) {
                continue;
            }
            Boolean checkUnique = columnMissConfig.getCheckUnique();
            if (checkUnique) {
                boolean b = columnsModelList.parallelStream().anyMatch(item -> item.getColumnKey().equals("PRI") || item.getColumnKey().equals("UNI"));
                tablesModel.setHaveUnique(b);
            }
            List<ColumnsModel> columnMissModelList = columnMissConfig.getColumnMissModelList();
            List<String> missFieldList = tablesModel.getMissFieldList() != null ? tablesModel.getMissFieldList() : new ArrayList<>();
            columnMissModelList.stream().forEach(item -> {
                boolean b = columnsModelList.stream().anyMatch(columnsModel -> columnsModel.getColumnName().equalsIgnoreCase(item.getColumnName()));
                if (!b) {
                    missFieldList.add(item.getColumnName());
                }
            });
            tablesModel.setMissField(Strings.join(missFieldList, ','));
            tablesModel.setMissFieldList(missFieldList);
        }


        return list;
    }
}
