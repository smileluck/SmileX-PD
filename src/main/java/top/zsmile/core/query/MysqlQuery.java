package top.zsmile.core.query;

import top.zsmile.core.model.TablesModel;

import java.util.List;

public class MysqlQuery implements Query {
    @Override
    public List<TablesModel> queryTables(String databaseName) {
        return null;
    }

    @Override
    public void queryColumns(String tableName) {

    }

    @Override
    public void queryCreateTableSql(String tableName) {

    }
}
