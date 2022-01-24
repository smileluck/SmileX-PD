package top.zsmile.demo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import sun.reflect.annotation.ExceptionProxy;
import top.zsmile.core.config.FreemakerConfig;
import top.zsmile.core.constant.DefaultConstants;
import top.zsmile.core.execute.WordExecute;
import top.zsmile.core.filter.FilterConfig;
import top.zsmile.core.filter.ProcessFilter;
import top.zsmile.core.model.ColumnsModel;
import top.zsmile.core.model.DatabaseModel;
import top.zsmile.core.model.TablesModel;
import top.zsmile.core.query.MysqlQuery;
import top.zsmile.core.utils.DataSourceUtils;
import top.zsmile.core.utils.ModelUtils;
import top.zsmile.core.utils.ResultSetUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        WordExecute wordExecute = new WordExecute();
        wordExecute.executeWord(dataMap);
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
        String sql = mysqlQuery.queryCreateTableSql("geek_shop.tb_order_good");
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
                    String sql = mysqlQuery.queryCreateTableSql(databaseName + "." + tablesModel.getTableName());
                    System.out.println(sql);
                    statement.execute(sql);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        List<String> assignTableNames = new ArrayList<String>();
        assignTableNames.add("common_constants");
        assignTableNames.add("mt_play_log");
        assignTableNames.add("mt_product_info");
        assignTableNames.add("mt_product_sale_backup");
        assignTableNames.add("mt_push_error");
        assignTableNames.add("mt_shop");
        assignTableNames.add("mt_shop_play");
        assignTableNames.add("mt_shop_play_log");
        assignTableNames.add("tb_article");
        assignTableNames.add("tb_article_category");
        assignTableNames.add("tb_comment");
        assignTableNames.add("tb_comment_category");
        assignTableNames.add("tb_vlog");
        assignTableNames.add("w_access");
        assignTableNames.add("w_admin");
        assignTableNames.add("w_certificate");
        assignTableNames.add("w_certificate_log");
        assignTableNames.add("w_certificate_voucher");
        assignTableNames.add("w_opera");
        assignTableNames.add("w_opera_audit");
        assignTableNames.add("w_play_htyp_sale");
        assignTableNames.add("w_play_sale_log");
        assignTableNames.add("w_play_sale_total_log");
        assignTableNames.add("w_publisher");
        assignTableNames.add("w_region_new");
        assignTableNames.add("w_shop");
        assignTableNames.add("w_tags");
        assignTableNames.add("w_writer");

        FilterConfig filterConfig = FilterConfig.builder().assignTableName(assignTableNames).build();
        List<String> list = new ArrayList<>();
//        list.add("localhost");
//        list.add("black_probe_ms");
//        list.add("content_db");
//        list.add("heitan_db");
//        list.add("heitan_open");
//        list.add("heitan_pay");
        list.add("wolf_data_db");
        test5(list, filterConfig);
//
//        System.out.println(list.contains("heitan_pay"));
//        System.out.println(list.contains("heitan_Pay"));
//        System.out.println(list.contains("heitan_ay"));
    }
}

