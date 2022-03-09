package top.zsmile.flink.demo;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.connector.jdbc.JdbcInputFormat;
import org.apache.flink.connector.jdbc.catalog.JdbcCatalog;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.types.Row;

public class FlinkSqlDemo {
    public static void main1(String[] args) {

//        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
//        settings.
        TableEnvironment tEnv = TableEnvironment.create(settings);
        JdbcCatalog dw = new JdbcCatalog("heytime_dw", "heytime_ods", "xiaoheitan", "AOX$VwSb$K!0xxVK", "jdbc:mysql://am-bp1un0u8625yb43s7167320o.ads.aliyuncs.com:3306");
        tEnv.registerCatalog("dw", dw);
        tEnv.useCatalog("dw");

//        DataStreamSource<Row> dataSource = tEnv.sq(jdbcInputFormat);
//        JdbcCatalog

//        Table table1 = tEnv.fromDataStream(dataSource);
        Table table = tEnv.sqlQuery("select * from dwd_coupon_user_test limit 100000");
//        table.map()
//
//        DataStream<Row> rowDataSet = tEnv.toDataStream(table1);
        TableResult execute = table.execute();
        execute.print();
    }

    public static void main2(String[] args) throws Exception {


        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
//        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);
        JdbcInputFormat jdbcInputFormat = JdbcInputFormat.buildJdbcInputFormat()
                .setDrivername("com.mysql.cj.jdbc.Driver")
                .setDBUrl("jdbc:mysql://localhost:3306/flink_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai")
                .setUsername("root")
                .setPassword("123456")
                .setQuery("select dept_id,dept_name from dep_emp")
                .setRowTypeInfo(new RowTypeInfo(BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO))
                .finish();
        DataSource<Row> dataSource = env.createInput(jdbcInputFormat);

//        JdbcRowOutputFormat xiaoheitan = JdbcRowOutputFormat.buildJdbcOutputFormat()
//                .setDrivername("com.mysql.cj.jdbc.Driver")
//                .setDBUrl("jdbc:mysql://localhost:3306/flink_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai")
//                .setUsername("root")
//                .setPassword("123456")
//                .setQuery("INSERT INTO `heytime_dw`.`dwd_coupon_user_test1` (`pk_id`, `coupon_user_id`, `coupon_type`, `coupon_id`, `user_id`, `status`, `operating_time`, `invalid_time`, `coupon_name`, `scope_type`, `goods_ids`, `discount`, `create_time`, `elt_del_flag`, `elt_create_time`, `elt_update_time`, `reduce_cost`, `least_cost`) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?,?,?);\n")
//                .finish();
//
//        dataSource.output(xiaoheitan);


//        JdbcRowOutputFormat output = JdbcRowOutputFormat.buildJdbcOutputFormat()
//                .setDrivername("com.mysql.cj.jdbc.Driver")
//                .setDBUrl("jdbc:mysql://localhost:3306/flink_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai")
//                .setUsername("root")
//                .setPassword("123456")
//                .setQuery("INSERT INTO `dep_emp` (`dept_id`, `dept_name`) VALUES (?, ?);")
//                .finish();
//
//        dataSource.output(output);


//        Table table1 = tEnv.fromDataStream(dataSource);

//        DataStream<Row> rowDataSet = tEnv.toDataStream(table1);
//        rowDataSet.print();
        dataSource.print();

        env.execute("test");

    }


    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        StreamTableEnvironment tEnv = StreamTableEnvironment.create(env);
        JdbcInputFormat jdbcInputFormat = JdbcInputFormat.buildJdbcInputFormat()
                .setDrivername("com.mysql.cj.jdbc.Driver")
                .setDBUrl("jdbc:mysql://localhost:3306/flink_test?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai")
                .setUsername("root")
                .setPassword("123456")
                .setQuery("select pk_id,wechat_openid,user_id,wechat_nickname from dwd_user limit 30000")
                .setRowTypeInfo(new RowTypeInfo(BasicTypeInfo.LONG_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.LONG_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO))
                .finish();
        DataStreamSource<Row> dataSource = env.createInput(jdbcInputFormat);

//        Table table1 = tEnv.fromDataStream(dataSource);

//        DataStream<Row> rowDataSet = tEnv.toDataStream(table1);
//        rowDataSet.print();
        dataSource.print();

        dataSource.setParallelism(5).addSink(new MyJdbcSink());
        env.execute("test");

    }

}
