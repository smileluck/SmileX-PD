package top.zsmile.canal.process;

import com.alibaba.excel.util.DateUtils;
import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.extern.slf4j.Slf4j;
import top.zsmile.core.handler.replace.ReplaceConfig;
import top.zsmile.core.handler.replace.TableNameReplace;
import top.zsmile.core.model.TablesModel;
import top.zsmile.core.query.MysqlDataQuery;
import top.zsmile.core.utils.DataSourceUtils;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Slf4j
public class EtlProcess {

    private static final String toDatabaseName = "heytime_ods";

    private static List<TablesModel> tablesModels = null;

    private static List<String> logicDelFields = new ArrayList<>();

    static {
        logicDelFields.add("deleted");
        logicDelFields.add("flag");
        logicDelFields.add("del");
    }

    public static void operateEtl(CanalEntry.EventType eventType, String schemaName, String tableName, CanalEntry.RowData rowData) {
        List<String> replacePrefix = new ArrayList<>();
        replacePrefix.add("tb_");
        replacePrefix.add("t_");
        replacePrefix.add("w_");

        TableNameReplace tableNameReplace = new TableNameReplace(ReplaceConfig.builder(schemaName + "_").replaceTablePrefix(replacePrefix).build());

        String toTableName = tableNameReplace.replace(tableName);
        if (tableName.equalsIgnoreCase("w_certificate")) {
            toTableName += "_1";
        }

        checkTables(toTableName);

        if (eventType == CanalEntry.EventType.DELETE) {
            deleteEtl(toTableName, rowData.getBeforeColumnsList());
        } else if (eventType == CanalEntry.EventType.INSERT) {
            insertEtl(toTableName, rowData.getAfterColumnsList());
        } else {
            updateEtl(toTableName, rowData);
        }

    }

    private static boolean checkTables(String toTableName) {
        if (tablesModels == null) {
            MysqlDataQuery mysqlQuery = new MysqlDataQuery();
            tablesModels = mysqlQuery.queryTables(toDatabaseName);
        }
        log.info("checkTable,search table:" + toTableName);
        for (TablesModel tablesModel : tablesModels) {
            if (tablesModel.getTableName().equalsIgnoreCase(toTableName)) {

                return true;
            }
        }
        log.error("checkTable,no table:" + toTableName);
        return false;
    }

    private static void insertEtl(String tableName, List<CanalEntry.Column> columns) {
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection();
            Statement statement = connection.createStatement();
            String dateFormat = DateUtils.format(new Date());
            for (CanalEntry.Column column : columns) {
                if (column.getIsKey()) {
                    String sql = "update " + toDatabaseName + "." + tableName +
                            " set elt_create_time = '" + dateFormat + "',elt_update_time = '" + dateFormat + "' where " + column.getName() + "=" + column.getValue();

                    int i = statement.executeUpdate(sql);
                    System.out.println("insertEtl result: " + i + "   Sql：" + sql);
                    break;
                }
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceUtils.closeConnection(connection);
        }
    }

    private static void updateEtl(String tableName, CanalEntry.RowData rowData) {
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection();
            Statement statement = connection.createStatement();
            String dateFormat = DateUtils.format(new Date());
            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
            int keyIndex = -1, delIndex = -1;
            for (int i = 0; i < afterColumnsList.size(); i++) {
                CanalEntry.Column column = afterColumnsList.get(i);
                if (column.getIsKey()) {
                    keyIndex = i;
                    break;
                }
//                if (column.getUpdated()) {
//
//                }
            }
            CanalEntry.Column keyColumn = afterColumnsList.get(keyIndex);
            StringBuilder sql = new StringBuilder();
            sql.append("update " + toDatabaseName + "." + tableName + " set elt_update_time = '" + dateFormat + "'");
//
//            if (delIndex != -1) {
//                CanalEntry.Column delColumn = afterColumnsList.get(delIndex);
//                sql.append(",elt_del_flag=" + 0);
//            }

            sql.append(" where " + keyColumn.getName() + " = " + keyColumn.getValue());

            log.info("update Sql => "  + sql.toString());
            int i = statement.executeUpdate(sql.toString());
            System.out.println("updateEtl result: " + i + "   Sql：" + sql.toString());

        } catch (
                SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceUtils.closeConnection(connection);
        }
    }

    private static void deleteEtl(String tableName, List<CanalEntry.Column> columns) {
        Connection connection = null;
        try {
            connection = DataSourceUtils.getConnection();
            Statement statement = connection.createStatement();
            String dateFormat = DateUtils.format(new Date());
            for (CanalEntry.Column column : columns) {
                if (column.getIsKey()) {
                    String sql = "update " + toDatabaseName + "." + tableName +
                            " set elt_update_time = '" + dateFormat + "',elt_del_flag=1 where " + column.getName() + "=" + column.getValue();

                    log.info("logic Del Sql => " + sql.toString());
                    int i = statement.executeUpdate(sql);
                    System.out.println("logic delete result: " + i + "   Sql：" + sql);
                    break;
                }
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceUtils.closeConnection(connection);
        }
    }

    // 值为删除的值
    private static Map<String, String> tablelogicDelMaps = new HashMap<>();


    static {
        // flag
        tablelogicDelMaps.put("black_probe_ms_", "0");
        // del,deleted
        tablelogicDelMaps.put("content_db_", "1");
        // del,deleted 100,is_del,state 4
        tablelogicDelMaps.put("heitan_db_", "1|100");
    }

    private static int checkLoginDel(String tableName, CanalEntry.Column column) {
        String name = column.getName();
        String value = column.getValue();


        return 0;
    }
}
