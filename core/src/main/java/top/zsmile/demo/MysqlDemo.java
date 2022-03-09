package top.zsmile.demo;

import com.alibaba.excel.EasyExcel;
import top.zsmile.core.entity.dto.ColumnAddDTO;
import top.zsmile.core.entity.dto.ColumnChangeDTO;
import top.zsmile.core.entity.vo.ReplaceTableVO;
import top.zsmile.core.handler.filter.TableFilter;
import top.zsmile.core.handler.filter.TableFilterConfig;
import top.zsmile.core.handler.replace.ReplaceConfig;
import top.zsmile.core.handler.replace.SqlForeignKeyReplace;
import top.zsmile.core.handler.replace.TableNameReplace;
import top.zsmile.core.model.IndexModel;
import top.zsmile.core.model.TablesModel;
import top.zsmile.core.query.MysqlDataManipulation;
import top.zsmile.core.query.MysqlDataQuery;
import top.zsmile.core.utils.DataSourceUtils;
import top.zsmile.core.utils.ModelUtils;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlDemo {
    public static void searchAndMergeIndex() {
        String databaseName = "smilex-pd";
        MysqlDataManipulation mysqlDrop = new MysqlDataManipulation();

        MysqlDataQuery mysqlQuery = new MysqlDataQuery();

        List<IndexModel> indexModels = mysqlQuery.queryIndex(databaseName, "demo");

        List list = ModelUtils.mergeTableIndex(indexModels);
        System.out.println(list);
    }

    public static void dropIndex() {
        String databaseName = "heitan_db";
        MysqlDataManipulation mysqlDrop = new MysqlDataManipulation();
        mysqlDrop.dropIndex(databaseName, "tb_certificate");


//        MysqlQuery mysqlQuery = new MysqlQuery();
//        List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
//        for (TablesModel tablesModel : tablesModels) {
//            mysqlDrop.dropIndex(databaseName, tablesModel.getTableName());
//        }
    }

    public static void mergeDb(List<String> databaseNameList, TableFilterConfig tableFilterConfig) {
        for (String databaseName : databaseNameList) {
            MysqlDataQuery mysqlQuery = new MysqlDataQuery();
            List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
//            List<ColumnsModel> columnsModels = mysqlQuery.queryColumns(databaseName);

            List<TablesModel> filter = new TableFilter(tableFilterConfig).filter(tablesModels);

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

    public static void addColumn() {
        ColumnAddDTO build = ColumnAddDTO.builder().columnName("test").comment("测试").dataType("varchar").dataLen(255).defaultStr("ttttt").build();
        MysqlDataManipulation mysqlDataManipulation = new MysqlDataManipulation();
        mysqlDataManipulation.addColumn("smilex-pd", "demo", build);
    }

    public static void addColumn2() {

        String databaseName = "heytime_dw";
        MysqlDataManipulation mysqlDataManipulation = new MysqlDataManipulation();
        MysqlDataQuery mysqlQuery = new MysqlDataQuery();

        ColumnAddDTO etlCreateTime = ColumnAddDTO.builder().columnName("elt_create_time").comment("etl创建时间").dataType("timestamp").build();
        ColumnAddDTO etlUpdateTime = ColumnAddDTO.builder().columnName("elt_update_time").comment("etl更新时间").dataType("timestamp").build();
//        ColumnAddDTO eltDelFlag = ColumnAddDTO.builder().columnName("elt_del_flag").comment("etl逻辑删除,0未删除，1删除").defaultStr("0").dataType("smallint").build();


        List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
        int i = 1;
        for (TablesModel tablesModel : tablesModels) {
            System.out.println((i++) + "->" + tablesModel.getTableName());
            if (tablesModel.getTableName().equalsIgnoreCase("data_insert_time_record")) continue;
            mysqlDataManipulation.addColumn(databaseName, tablesModel.getTableName(), etlCreateTime);
            mysqlDataManipulation.addColumn(databaseName, tablesModel.getTableName(), etlUpdateTime);
//            mysqlDataManipulation.addColumn(databaseName, tablesModel.getTableName(), eltDelFlag);
        }
    }

    public static void delColumn() {
        String databaseName = "heytime_dw";
        MysqlDataManipulation mysqlDataManipulation = new MysqlDataManipulation();
        MysqlDataQuery mysqlQuery = new MysqlDataQuery();

        List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
        int i = 1;
        for (TablesModel tablesModel : tablesModels) {
            System.out.println(i++ + "->" + tablesModel.getTableName());
            if (tablesModel.getTableName().equalsIgnoreCase("heitan_db_test_ods")) continue;
            mysqlDataManipulation.dropColumn(databaseName, tablesModel.getTableName(), "elt_create_time");
            mysqlDataManipulation.dropColumn(databaseName, tablesModel.getTableName(), "elt_update_time");
        }
    }

    public static void changeColumnDefault() {

        String databaseName = "heytime_ods";
        MysqlDataManipulation mysqlDataManipulation = new MysqlDataManipulation();
        MysqlDataQuery mysqlQuery = new MysqlDataQuery();

        ColumnChangeDTO etlCreateTime = ColumnChangeDTO.builder().columnName("elt_create_time").comment("etl创建时间").dataType("timestamp").defaultStr("CURRENT_TIMESTAMP").build();
        ColumnChangeDTO etlUpdateTime = ColumnChangeDTO.builder().columnName("elt_update_time").comment("etl更新时间").dataType("timestamp").defaultStr("CURRENT_TIMESTAMP").build();
//        ColumnAddDTO eltDelFlag = ColumnAddDTO.builder().columnName("elt_del_flag").comment("etl逻辑删除").defaultStr("0").dataType("smallint").build();


        List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
        for (TablesModel tablesModel : tablesModels) {
            if (tablesModel.getTableName().equalsIgnoreCase("heitan_db_test_ods")) continue;
            mysqlDataManipulation.changeColumn(databaseName, tablesModel.getTableName(), etlCreateTime);
            mysqlDataManipulation.changeColumn(databaseName, tablesModel.getTableName(), etlUpdateTime);
//            mysqlDataManipulation.addColumn(databaseName, tablesModel.getTableName(), eltDelFlag);
        }
    }

    public static void copyTable() {
        String targetDbName = "heytime_dw";

        MysqlDataQuery mysqlQuery = new MysqlDataQuery();
        List<TablesModel> tablesModels = mysqlQuery.queryTables(targetDbName);

        for (TablesModel tablesModel : tablesModels) {
            String createTableSql = mysqlQuery.queryCreateTableSql(targetDbName, tablesModel.getTableName());
            createTableSql = createTableSql.replaceFirst(tablesModel.getTableName(), tablesModel.getTableName() + "_his");
            String[] sqlArray = createTableSql.split("\n");
            String subSql = sqlArray[sqlArray.length - 1];
            sqlArray[sqlArray.length - 1] = subSql.substring(0, subSql.lastIndexOf("'")) + "_历史记录'";
            String join = String.join("\n", sqlArray);
            mysqlQuery.querySql(join);
        }
    }

    public static void main(String[] args) {
//        searchAndMergeIndex();
//        addColumn2();

//        List<String> assignTableNames = new ArrayList<String>();
//
//        assignTableNames.add("common_constants");
//
//
//        FilterConfig filterConfig = FilterConfig.builder().assignTableName(assignTableNames).build();
//        List<String> list = new ArrayList<>();
////        list.add("localhost");
////        list.add("black_probe_ms");
////        list.add("content_db");
////        list.add("heitan_db");
////        list.add("heitan_open");
////        list.add("heitan_pay");
////        list.add("heytalk");
//        list.add("wolf_data_db");
//        mergeDb(list, filterConfig);
        addColumn2();
    }
}
