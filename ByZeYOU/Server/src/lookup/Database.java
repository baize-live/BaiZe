package lookup;

import java.sql.*;

public class Database {
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    //创建数据库
    public Database(String databaseName, String username, String password) {
        try {
            linkDatabase(databaseName, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // 这里有连接数据库的 地址等等  移植的时候记得改
    private void linkDatabase(String databaseName, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, username, password);
        statement = connection.createStatement();
    }

    //关闭数据库
    private void closeDatabase() {
        Runtime run = Runtime.getRuntime();//当前 Java 应用程序相关的运行时对象。
        //注册新的虚拟机来关闭钩子
        run.addShutdownHook(new Thread(() -> {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }));
    }

    public void closeDatabaseNow() {
        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
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
}
