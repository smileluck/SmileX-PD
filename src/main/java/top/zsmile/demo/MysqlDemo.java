package top.zsmile.demo;

import com.alibaba.excel.EasyExcel;
import top.zsmile.core.entity.vo.ReplaceTableVO;
import top.zsmile.core.handler.filter.FilterConfig;
import top.zsmile.core.handler.filter.ProcessFilter;
import top.zsmile.core.handler.replace.ReplaceConfig;
import top.zsmile.core.handler.replace.SqlForeignKeyReplace;
import top.zsmile.core.handler.replace.TableNameReplace;
import top.zsmile.core.model.TablesModel;
import top.zsmile.core.query.MysqlDrop;
import top.zsmile.core.query.MysqlQuery;
import top.zsmile.core.utils.DataSourceUtils;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlDemo {
    public static void test1() {
        String databaseName = "ods_original";
        MysqlDrop mysqlDrop = new MysqlDrop();

        MysqlQuery mysqlQuery = new MysqlQuery();

        List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
        for (TablesModel tablesModel : tablesModels) {
            mysqlDrop.dropIndex(databaseName, tablesModel.getTableName());
        }
    }

    public static void test2(List<String> databaseNameList, FilterConfig filterConfig) {
        for (String databaseName : databaseNameList) {
            MysqlQuery mysqlQuery = new MysqlQuery();
            List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
//            List<ColumnsModel> columnsModels = mysqlQuery.queryColumns(databaseName);

            List<TablesModel> filter = new ProcessFilter(filterConfig).filter(tablesModels);

            Map<String, Integer> repeatMap = new HashMap<>();

            try {
                Connection connection = DataSourceUtils.getConnection();
                connection.setAutoCommit(false);
                Statement statement = connection.createStatement();

                List<ReplaceTableVO> replaceTableVOS = new ArrayList<>();

                List<String> replacePrefix = new ArrayList<>();
                replacePrefix.add("tb_");
                replacePrefix.add("t_");
                replacePrefix.add("w_");

                TableNameReplace tableNameReplace = new TableNameReplace(ReplaceConfig.builder(databaseName + "_").replaceTablePrefix(replacePrefix).build());
                SqlForeignKeyReplace sqlForeignKeyReplace = new SqlForeignKeyReplace();

                for (TablesModel tablesModel : filter) {
                    ReplaceTableVO replaceTableVO = new ReplaceTableVO();
                    replaceTableVO.setFromDatabaseName(databaseName);
                    replaceTableVO.setToDatabaseName("ods_original");
                    String sql = mysqlQuery.queryCreateTableSql(databaseName, tablesModel.getTableName());

                    String toTableName = tableNameReplace.replace(tablesModel.getTableName());

                    Integer integer = repeatMap.getOrDefault(toTableName, null);
                    if (integer != null) {
                        integer++;
                        repeatMap.put(toTableName, integer);
                        toTableName += "_" + integer.intValue();
                    } else {
                        repeatMap.put(toTableName, 0);
                    }

                    String afterSql = sqlForeignKeyReplace.replace(sql.replace(tablesModel.getTableName(), toTableName));


//                    String afterSql = sqlReplace.replace(sql);

                    replaceTableVO.setFromTableName(tablesModel.getTableName());
                    replaceTableVO.setFromTableSql(sql);
                    replaceTableVO.setToTableName(toTableName);
                    replaceTableVO.setToTableSql(afterSql);

                    replaceTableVOS.add(replaceTableVO);

                    statement.execute(afterSql);
                }
                connection.commit();
                String fileName = databaseName + ".xlsx";
                // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
                // 如果这里想使用03 则 传入excelType参数即可
                EasyExcel.write(fileName, ReplaceTableVO.class)
                        .sheet("模板")
                        .doWrite(() -> {
                            // 分页查询数据
                            return replaceTableVOS;
                        });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        List<String> assignTableNames = new ArrayList<String>();

        assignTableNames.add("common_constants");


        FilterConfig filterConfig = FilterConfig.builder().assignTableName(assignTableNames).build();
        List<String> list = new ArrayList<>();
//        list.add("localhost");
//        list.add("black_probe_ms");
//        list.add("content_db");
//        list.add("heitan_db");
//        list.add("heitan_open");
//        list.add("heitan_pay");
//        list.add("heytalk");
        list.add("wolf_data_db");
        test2(list, filterConfig);
    }
}
