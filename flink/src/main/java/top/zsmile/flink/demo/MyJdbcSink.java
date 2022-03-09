package top.zsmile.flink.demo;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.types.Row;
import top.zsmile.flink.core.utils.DataSourceUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MyJdbcSink implements SinkFunction<Row> {

    private Connection connection;
    private PreparedStatement insertStmt;

    private final String updateSql = "INSERT INTO `dep_emp` (`dept_id`, `dept_name`) VALUES (?, ?);";
    private final String insertSql = "INSERT INTO `dwd_user_test` (pk_id,wechat_openid,user_id,wechat_nickname) VALUES (?, ?,?,?);";

//    @Override
//    public void open(Configuration parameters) throws Exception {
////        super.open(parameters);
//        connection = DataSourceUtils.getConnection();
//        insertStmt = connection.prepareStatement(insertSql);
//    }


//    @Override
//    public void invoke(DeptEntity value, Context context) throws Exception {
//        insertStmt.setInt(1, value.getDeptId());
//        insertStmt.setString(2, value.getDeptName());
//        insertStmt.execute();
//    }
//
//    @Override
//    public void close() throws Exception {
//        super.close();
//    }


    @Override
    public void invoke(Row value, Context context) throws Exception {
        if (connection == null || connection.isClosed()) {
            connection = DataSourceUtils.getConnection();
        }
        insertStmt = connection.prepareStatement(insertSql);
        insertStmt.setLong(1, (Long) value.getField(0));
        insertStmt.setString(2, value.getField(1) != null ? value.getField(1).toString() : "");
        insertStmt.setLong(3, (Long) value.getField(2));
        insertStmt.setString(4, value.getField(3) != null ? value.getField(3).toString() : "");
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void finish() throws Exception {
        System.out.println("over");
//        DataSourceUtils.closeConnection(connection);
    }
}
