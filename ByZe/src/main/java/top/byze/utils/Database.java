package top.byze.utils;

import java.sql.*;

/**
 * @author CodeXS
 */
public class Database {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    /**
     * 使用JDBC 连接数据库 创建数据库连接
     */
    public Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://114.116.89.58:3306/baizeDB", "root", "SS111827jj!");
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行有返回值的查询语句, 结果保存在resultSet
     *
     * @param sql sql语句
     */
    public void findDatabase(String sql) throws SQLException {
        resultSet = statement.executeQuery(sql);
    }

    /**
     * 执行无返回值的查询语句
     *
     * @param sql sql语句
     */
    public void workDatabase(String sql) throws SQLException {
        statement.executeUpdate(sql);
    }

    /**
     * 拿到返回结果
     *
     * @return resultSet
     */
    public ResultSet getResultSet() {
        return resultSet;
    }

    /**
     * 拿到连接对象
     *
     * @return connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * 关闭数据库
     */
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

