package top.zsmile.core.query;

import top.zsmile.core.model.IndexModel;
import top.zsmile.core.model.TablesModel;
import top.zsmile.core.utils.DataSourceUtils;
import top.zsmile.core.utils.ResultSetUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MysqlDrop implements Drop {

    @Override
    public boolean dropIndex(String databaseName, String tableName) {
        MysqlQuery query = new MysqlQuery();
        List<IndexModel> indexModels = query.queryIndex(databaseName, tableName);
        return dropIndex(databaseName, tableName, indexModels);
    }

    @Override
    public boolean dropIndex(String databaseName, String tableName, List<IndexModel> indexModels) {

        String tmp = "";
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection();
            Statement statement = connection.createStatement();

            for (IndexModel indexModel : indexModels) {
                if (tmp.equalsIgnoreCase(indexModel.getKeyName())) {
                    continue;
                }
                if (!indexModel.getKeyName().equals("PRIMARY")) {
                    String sql = "DROP INDEX `" + indexModel.getKeyName() + "` ON " + databaseName + "." + tableName + ";";
                    System.out.println(sql);
                    statement.execute(sql);
                }
            }
            return true;
        } catch (
                SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DataSourceUtils.closeConnection(connection);
        }
    }

    @Override
    public boolean dropIndex(String databaseName, String tableName, String indexName) {


        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection();
            Statement statement = connection.createStatement();

            String sql = "DROP INDEX " + indexName + " ON " + databaseName + "." + tableName + ";";
            boolean execute = statement.execute(sql);

            return execute;
        } catch (
                SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DataSourceUtils.closeConnection(connection);
        }
    }
}
