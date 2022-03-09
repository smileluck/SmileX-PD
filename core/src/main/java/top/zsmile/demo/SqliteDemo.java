package top.zsmile.demo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class SqliteDemo {

    public static void noDatasource(){
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
//            String resourcePath = SqliteDemo.class.getClassLoader().getResource("").getPath();
            connection = DriverManager.getConnection("jdbc:sqlite:db/test.db");
//            CallableStatement callableStatement = connection.prepareCall("select * from ttt");
            Statement statement = connection.createStatement();
//            String sql = "CREATE TABLE COMPANY " +
//                    "(ID INT PRIMARY KEY     NOT NULL," +
//                    " NAME           TEXT    NOT NULL, " +
//                    " AGE            INT     NOT NULL, " +
//                    " ADDRESS        CHAR(50), " +
//                    " SALARY         REAL)";
//            statement.executeUpdate(sql);

            String sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (7, 'Pau12312345354l', 32, 'California', 20000.00 );";
            statement.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (8, 'Paulf1231234534ff', 56, '123123', 111.00 );";
            statement.executeUpdate(sql);

            sql = "select * from  Company";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String address = resultSet.getString("address");
                float salary = resultSet.getFloat("salary");
                System.out.println("ID = " + id);
                System.out.println("NAME = " + name);
                System.out.println("AGE = " + age);
                System.out.println("ADDRESS = " + address);
                System.out.println("SALARY = " + salary);
                System.out.println();
            }

            statement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void hasDatasource(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:sqlite:db/test.db");
        hikariConfig.setPoolName("SQLiteConnectionPool");
        hikariConfig.setDriverClassName("org.sqlite.JDBC");
        hikariConfig.setAutoCommit(false);
        hikariConfig.setMinimumIdle(8);
        hikariConfig.setMaximumPoolSize(32);
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        try {
            Connection connection = hikariDataSource.getConnection();
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (9, 'Pau12312345354l', 32, 'California', 20000.00 );";
            statement.executeUpdate(sql);

            sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) " +
                    "VALUES (10, 'Paulf1231234534ff', 56, '123123', 111.00 );";
            statement.executeUpdate(sql);

            sql = "select * from  Company";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String address = resultSet.getString("address");
                float salary = resultSet.getFloat("salary");
                System.out.println("ID = " + id);
                System.out.println("NAME = " + name);
                System.out.println("AGE = " + age);
                System.out.println("ADDRESS = " + address);
                System.out.println("SALARY = " + salary);
                System.out.println();
            }
            statement.close();
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        hasDatasource();
    }
}
