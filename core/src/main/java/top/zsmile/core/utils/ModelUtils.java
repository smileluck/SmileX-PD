package top.zsmile.core.utils;

import com.mysql.cj.xdevapi.Column;
import top.zsmile.core.model.ColumnsModel;
import top.zsmile.core.model.IndexModel;
import top.zsmile.core.model.TablesModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelUtils {

    /**
     * 合并表和字段
     * @param tableList
     * @param columnList
     * @return
     */
    public static List mergeTableAndColumn(List<TablesModel> tableList, List<ColumnsModel> columnList) {
        Map<String, List<ColumnsModel>> collect = columnList.stream().collect(Collectors.groupingBy(ColumnsModel::getTableName));
        for (TablesModel tablesModel : tableList) {
            String tableName = tablesModel.getTableName();
            List<ColumnsModel> columnsModels = collect.get(tableName);
            if (columnsModels != null) {
                tablesModel.setColumnsModelList(columnsModels);
                continue;
            }
        }
        return tableList;
    }


    /**
     * 合并表和索引
     * @param tableList
     * @param indexModelList
     * @return
     */
    public static List mergeTableAndIndex(List<TablesModel> tableList, List<IndexModel> indexModelList) {
        Map<String, List<IndexModel>> collect = indexModelList.stream().collect(Collectors.groupingBy(IndexModel::getTableName));
        for (TablesModel tablesModel : tableList) {
            String tableName = tablesModel.getTableName();
            List<IndexModel> indexModels = collect.get(tableName);
            if (indexModels != null) {
                tablesModel.setIndexModelList(indexModels);
                continue;
            }
        }
        return tableList;
    }

    /**
     * same key-name merge more field to one
     *
     * @return
     */
    public static List mergeTableIndex(List<IndexModel> indexModels) {
        List<IndexModel> mergeResult = indexModels.parallelStream().collect(Collectors.toMap(IndexModel::getKeyName, a -> a, (o1, o2) -> {
            o1.setShowColumnName(o1.getShowColumnName() + "," + o2.getShowColumnName());
            return o1;
        })).values().stream().collect(Collectors.toList());
        return mergeResult;
    }

}
