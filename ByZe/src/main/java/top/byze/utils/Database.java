package top.byze.utils;

import java.sql.*;

public class Database {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    //创建数据库连接
    public Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://114.116.89.58:3306/baizeDB", "root", "SS111827jj!");
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //有返回
    public void findDatabase(String sql) throws SQLException {
        resultSet = statement.executeQuery(sql);
    }

    //无返回
    public void workDatabase(String sql) throws SQLException {
        statement.executeUpdate(sql);
    }

    //返回结果
    public ResultSet getResultSet() {
        return resultSet;
    }

    // 返回连接
    public Connection getConnection() {
        return connection;
    }

    //关闭数据库
    public void closeDatabase() {
        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

