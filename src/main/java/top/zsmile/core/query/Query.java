package top.zsmile.core.query;

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
     * @param tableName
     */
    void queryColumns(String tableName);

    /**
     * 查询建表语句
     *
     * @param tableName
     */
    void queryCreateTableSql(String tableName);
}
