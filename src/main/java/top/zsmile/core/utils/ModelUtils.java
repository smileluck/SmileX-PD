package top.zsmile.core.utils;

import com.mysql.cj.xdevapi.Column;
import top.zsmile.core.model.ColumnsModel;
import top.zsmile.core.model.TablesModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelUtils {

    public static List mergeTableAndColumn(List<TablesModel> tableList, List<ColumnsModel> columnList) {
        Map<String, List<ColumnsModel>> collect = columnList.stream().collect(Collectors.groupingBy(ColumnsModel::getTableName));
        for (TablesModel tablesModel : tableList) {
            String tableName = tablesModel.getTableName();
            List<ColumnsModel> columnsModels = collect.get(tableName);
            if (columnsModels != null) {
                tablesModel.setColumnsModelList(columnsModels);
            }
        }
        return tableList;
    }
}
