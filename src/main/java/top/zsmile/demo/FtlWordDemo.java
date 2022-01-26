package top.zsmile.demo;

import com.alibaba.excel.EasyExcel;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import top.zsmile.core.config.FreemakerConfig;
import top.zsmile.core.constant.DefaultConstants;
import top.zsmile.core.entity.vo.ReplaceTableVO;
import top.zsmile.core.handler.filter.FilterConfig;
import top.zsmile.core.handler.filter.ProcessFilter;
import top.zsmile.core.handler.replace.ReplaceConfig;
import top.zsmile.core.handler.replace.TableNameReplace;
import top.zsmile.core.model.ColumnsModel;
import top.zsmile.core.model.DatabaseModel;
import top.zsmile.core.model.IndexModel;
import top.zsmile.core.model.TablesModel;
import top.zsmile.core.process.WordProcess;
import top.zsmile.core.query.MysqlQuery;
import top.zsmile.core.utils.DataSourceUtils;
import top.zsmile.core.utils.ModelUtils;
import top.zsmile.core.utils.ResultSetUtils;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FtlWordDemo {

    public static void test1() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/cloud_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai");
        config.setUsername("root");
        config.setPassword("root");
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        try {

            Connection connection = hikariDataSource.getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT\n" +
                    "\tTABLE_NAME as tableName,\n" +
                    "\tTABLE_COMMENT as tableComment,\n" +
                    "\tENGINE\n" +
                    "FROM\n" +
                    "\tinformation_schema.TABLES\n" +
                    "WHERE\n" +
                    "\ttable_schema = \"geek_shop\"\n" +
                    "ORDER BY\n" +
                    "\tTABLE_NAME";
            ResultSet resultSet = statement.executeQuery(sql);

            List list = ResultSetUtils.convertClassList(resultSet, TablesModel.class);

            sql = "SELECT\n" +
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
                    "\tA.TABLE_SCHEMA = \"geek_shop\"";
//                    "AND A.TABLE_NAME = \"camera\"";

            resultSet = statement.executeQuery(sql);

            List columnList = ResultSetUtils.convertClassList(resultSet, ColumnsModel.class);

            List mergeList = ModelUtils.mergeTableAndColumn(list, columnList);


            DatabaseModel databaseModel = new DatabaseModel();
            databaseModel.setDescribe("数据库介绍");
            databaseModel.setVersion("v1.0");
            databaseModel.setName(DefaultConstants.NAME);

            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("tables", mergeList);
            dataMap.put("db", databaseModel);


            Template template = FreemakerConfig.INSTANCE.getTemplate("document-word.ftl");
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("/test.docx")), "UTF-8"));
            //FreeMarker使用Word模板和数据生成Word文档
            template.process(dataMap, out);


            Thread.sleep(1500);

            statement.close();
            connection.close();
        } catch (SQLException | IOException | TemplateException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void test2(String databaseName) {
        MysqlQuery mysqlQuery = new MysqlQuery();
        List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
        List<ColumnsModel> columnsModels = mysqlQuery.queryColumns(databaseName);

        List mergeList = ModelUtils.mergeTableAndColumn(tablesModels, columnsModels);

        DatabaseModel databaseModel = new DatabaseModel();
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("tables", mergeList);
        dataMap.put("db", databaseModel);

        WordProcess wordExecute = new WordProcess();
        wordExecute.process(dataMap);
    }

    public static void test22(List<String> databaseNameList) {
        for (String databaseName : databaseNameList) {
            MysqlQuery mysqlQuery = new MysqlQuery();
            List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
            List<ColumnsModel> columnsModels = mysqlQuery.queryColumns(databaseName);

            List mergeList = ModelUtils.mergeTableAndColumn(tablesModels, columnsModels);

            DatabaseModel databaseModel = new DatabaseModel();
            databaseModel.setName(databaseName);
            databaseModel.setDescribe("数仓-" + databaseName);
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("tables", mergeList);
            dataMap.put("db", databaseModel);

//            WordExecute wordExecute = new WordExecute();
//            wordExecute.executeWord(dataMap);
            try {
                Template template = FreemakerConfig.INSTANCE.getTemplate("document-word.ftl");
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("doc/processDoc/" + databaseName + ".docx")), "UTF-8"));
                //FreeMarker使用Word模板和数据生成Word文档
                template.process(dataMap, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void test3() {
        MysqlQuery mysqlQuery = new MysqlQuery();
        String sql = mysqlQuery.queryCreateTableSql("geek_shop", "tb_order_good");
        System.out.println(sql);
    }

    public static void test4(List<String> databaseNameList, FilterConfig filterConfig) {
        for (String databaseName : databaseNameList) {
            MysqlQuery mysqlQuery = new MysqlQuery();
            List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
            List<ColumnsModel> columnsModels = mysqlQuery.queryColumns(databaseName);
            List filter = new ProcessFilter(filterConfig).filter(tablesModels);

            List mergeList = ModelUtils.mergeTableAndColumn(filter, columnsModels);


            DatabaseModel databaseModel = new DatabaseModel();
            databaseModel.setName(databaseName);
            databaseModel.setDescribe("数仓-" + databaseName);
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("tables", mergeList);
            dataMap.put("db", databaseModel);


//            WordExecute wordExecute = new WordExecute();
//            wordExecute.executeWord(dataMap);
            try {
                Template template = FreemakerConfig.INSTANCE.getTemplate("document-word.ftl");
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("doc/processDoc/" + databaseName + ".docx")), "UTF-8"));
                //FreeMarker使用Word模板和数据生成Word文档
                template.process(dataMap, out);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void test5(List<String> databaseNameList, FilterConfig filterConfig) {
        for (String databaseName : databaseNameList) {
            MysqlQuery mysqlQuery = new MysqlQuery();
            List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
//            List<ColumnsModel> columnsModels = mysqlQuery.queryColumns(databaseName);

            List<TablesModel> filter = new ProcessFilter(filterConfig).filter(tablesModels);

            try {
                Connection connection = DataSourceUtils.getConnection();
                Statement statement = connection.createStatement();

                for (TablesModel tablesModel : filter) {
                    String sql = mysqlQuery.queryCreateTableSql(databaseName, tablesModel.getTableName());
                    System.out.println(sql);
                    statement.execute(sql);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void test55(List<String> databaseNameList, FilterConfig filterConfig) {
        for (String databaseName : databaseNameList) {
            MysqlQuery mysqlQuery = new MysqlQuery();
            List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
//            List<ColumnsModel> columnsModels = mysqlQuery.queryColumns(databaseName);

            List<TablesModel> filter = new ProcessFilter(filterConfig).filter(tablesModels);

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

                for (TablesModel tablesModel : filter) {
                    ReplaceTableVO replaceTableVO = new ReplaceTableVO();
                    replaceTableVO.setFromDatabaseName(databaseName);
                    replaceTableVO.setToDatabaseName("ods_original");
                    String sql = mysqlQuery.queryCreateTableSql(databaseName, tablesModel.getTableName());


                    String toTableName = tableNameReplace.replace(tablesModel.getTableName());

                    String afterSql = sql.replace(tablesModel.getTableName(), toTableName);
//                    String afterSql = sqlReplace.replace(sql);

                    replaceTableVO.setFromTableName(tablesModel.getTableName());
                    replaceTableVO.setFromTableSql(sql);
                    replaceTableVO.setToTableName(toTableName);
                    replaceTableVO.setToTableSql(afterSql);

                    replaceTableVOS.add(replaceTableVO);
                    statement.execute(afterSql);
                }
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

    public static void test6(List<String> databaseNameList, FilterConfig filterConfig) {
//        for (String databaseName : databaseNameList) {
        MysqlQuery mysqlQuery = new MysqlQuery();
//            List<TablesModel> tablesModels = mysqlQuery.queryTables(databaseName);
        List<IndexModel> indexModels = mysqlQuery.queryIndex("heitan_db", "tb_certificate");
        System.out.println(indexModels);
//            List<ColumnsModel> columnsModels = mysqlQuery.queryColumns(databaseName);

//        }
    }

    public static void main(String[] args) {
        List list = new ArrayList<>();
        list.add("ods_original");
        test22(list);
//
//        test55(list, filterConfig);
//        test6(null, null);
//        System.out.println(list.contains("heitan_pay"));
//        System.out.println(list.contains("heitan_Pay"));
//        System.out.println(list.contains("heitan_ay"));
    }
}

