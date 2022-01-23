package top.zsmile.core.query;

import top.zsmile.core.model.ColumnsModel;
import top.zsmile.core.model.TablesModel;

import java.util.List;

public interface Query {
    /**
     * 查询表信息
     *
     * @param databaseName
     * @return
     */
    List<TablesModel> queryTables(String databaseName);

    /**
     * 查询表结构信息
     *
     * @param databaseName
     */
    List<ColumnsModel> queryColumns(String databaseName);

    /**
     * 查询建表语句
     *
     * @param tableName
     */
    String queryCreateTableSql(String tableName);


}
