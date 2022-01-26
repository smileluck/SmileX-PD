package top.zsmile.core.query;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import top.zsmile.core.model.ColumnsModel;
import top.zsmile.core.model.IndexModel;
import top.zsmile.core.model.TablesModel;
import top.zsmile.core.model.convert.IndexModelConvert;
import top.zsmile.core.utils.DataSourceUtils;
import top.zsmile.core.utils.ResultSetUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MysqlQuery implements Query {
    @Override
    public List<TablesModel> queryTables(String databaseName) {
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection();
            Statement statement = connection.createStatement();

            String sql = "SELECT\n" +
                    "\tTABLE_NAME as tableName,\n" +
                    "\tTABLE_COMMENT as tableComment,\n" +
                    "\tENGINE\n" +
                    "FROM\n" +
                    "\tinformation_schema.TABLES\n" +
                    "WHERE\n" +
                    "\ttable_schema = \"" + databaseName + "\"\n" +
                    "ORDER BY\n" +
                    "\tTABLE_NAME";
            ResultSet resultSet = statement.executeQuery(sql);

            List list = ResultSetUtils.convertClassList(resultSet, TablesModel.class);
            return list;
        } catch (
                SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DataSourceUtils.closeConnection(connection);
        }

    }

    @Override
    public List<ColumnsModel> queryColumns(String databaseName) {
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT\n" +
                    "\tA.TABLE_NAME as tableName,\n" +
                    "\tA.COLUMN_NAME as columnName,\n" +
                    "\tA.COLUMN_TYPE as columnType,\n" +
                    "\tA.DATA_TYPE as dataType,\n" +
                    "CASE\n" +
                    "WHEN LOCATE('(', A.COLUMN_TYPE) > 0 THEN\n" +
                    "\tREPLACE (\n" +
                    "\t\tsubstring(\n" +
                    "\t\t\tA.COLUMN_TYPE,\n" +
                    "\t\t\tLOCATE('(', A.COLUMN_TYPE) + 1\n" +
                    "\t\t),\n" +
                    "\t\t')',\n" +
                    "\t\t''\n" +
                    "\t)\n" +
                    "ELSE\n" +
                    "\tNULL\n" +
                    "END dataLength\n," +
                    "\tA.IS_NULLABLE as isNullable\n," +
                    "\tA.COLUMN_KEY as columnKey\n," +
                    "\tA.EXTRA as extra\n," +
                    "\tA.COLUMN_COMMENT as columnComment,\n" +
                    "\tA.COLUMN_DEFAULT as columnDefault\n" +
                    "FROM\n" +
                    "\tINFORMATION_SCHEMA. COLUMNS A\n" +
                    "WHERE\n" +
                    "\tA.TABLE_SCHEMA = \"" + databaseName + "\"\n" +
                    "ORDER BY A.TABLE_NAME,A.ORDINAL_POSITION";
            ResultSet resultSet = statement.executeQuery(sql);
            List columnList = ResultSetUtils.convertClassList(resultSet, ColumnsModel.class);
            return columnList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DataSourceUtils.closeConnection(connection);
        }
    }

    @Override
    public String queryCreateTableSql(String databaseName, String tableName) {
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection();
            Statement statement = connection.createStatement();
            String createTableSql = null;
            String sql = "SHOW CREATE TABLE " + databaseName + "." + tableName + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                createTableSql = resultSet.getString("Create Table");
            }
            return createTableSql;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DataSourceUtils.closeConnection(connection);
        }
    }

    @Override
    public List<IndexModel> queryIndex(String databaseName, String tableName) {

        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection();
            Statement statement = connection.createStatement();

            String sql = "SHOW INDEX FROM " + databaseName + "." + tableName + ";";

            ResultSet resultSet = statement.executeQuery(sql);
            List<IndexModel> indexList = ResultSetUtils.convertClassList(resultSet, IndexModel.class, IndexModelConvert.INSTANCE);
            return indexList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DataSourceUtils.closeConnection(connection);
        }
    }
}
