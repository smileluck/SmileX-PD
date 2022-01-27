package top.zsmile.core.query;

import top.zsmile.core.entity.dto.ColumnAddDTO;
import top.zsmile.core.model.IndexModel;
import top.zsmile.core.utils.DataSourceUtils;
import top.zsmile.core.utils.ModelUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MysqlDataManipulation implements DataManipulation {

    @Override
    public boolean dropIndex(String databaseName, String tableName) {
        MysqlDataQuery query = new MysqlDataQuery();
        List<IndexModel> indexModels = query.queryIndex(databaseName, tableName);
        return dropIndex(databaseName, tableName, indexModels);
    }

    @Override
    public boolean dropIndex(String databaseName, String tableName, List<IndexModel> indexModels) {
        indexModels = ModelUtils.mergeTableIndex(indexModels);

        String tmp = "";
        Connection connection = null;
        List<String> map = new ArrayList<>();
        try {
            connection = DataSourceUtils.getConnection();
            Statement statement = connection.createStatement();

            for (IndexModel indexModel : indexModels) {
                if (tmp.equalsIgnoreCase(indexModel.getKeyName())) {
                    continue;
                }
                if (!indexModel.getKeyName().equals("PRIMARY")) {
                    try {
                        String sql = "DROP INDEX `" + indexModel.getKeyName() + "` ON " + databaseName + "." + tableName + ";";
                        System.out.println(sql);
                        statement.execute(sql);
                    } catch (SQLException e) {
                        map.add(indexModel.getKeyName());
                        e.printStackTrace();
                    }
                }
            }
            System.out.println(map);
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

    @Override
    public boolean dropColumn(String databaseName, String tableName, String columnName) {
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection();
            Statement statement = connection.createStatement();
            String sql = "ALTER TABLE " + databaseName + "." + tableName + " DROP " + columnName + ";";
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

    @Override
    public boolean addColumn(String databaseName, String tableName, ColumnAddDTO columnAddDTO) {
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection();
            Statement statement = connection.createStatement();
            String sql = "ALTER TABLE `" + databaseName + "`.`" + tableName
                    + "` " + columnAddDTO.toSql();
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
