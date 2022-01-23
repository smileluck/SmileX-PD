package top.zsmile.demo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import top.zsmile.core.config.FreemakerConfig;
import top.zsmile.core.constant.DefaultConstants;
import top.zsmile.core.model.ColumnsModel;
import top.zsmile.core.model.DatabaseModel;
import top.zsmile.core.model.TablesModel;
import top.zsmile.core.utils.ModelUtils;
import top.zsmile.core.utils.ResultSetUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

public class FtlWordDemo {

    public static void main(String[] args) {
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

}
