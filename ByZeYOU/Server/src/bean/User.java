package bean;

import lookup.Database;

public class User {
    /*
     * 记录用户信息
     * */
    private final String id;  // 这个是唯一标识
    private String name;
    private String state;    // 用来表示准备好了没
    private String image;
    private String identity; // 下棋人还是观棋人

    public User(String id) {
        this.id = id;
        this.name = getUserName(id);
        this.state = "0";
        this.image = getUserImage(id);
    }

    public static String getUserName(String id) {

        Database database = new Database("gobangdatabase", "root", "SS111827jj!");
        try {
            String sql = "select * from gobanguser where id =" + id + ";";
            database.findDatabase(sql);
            while (database.getResultSet().next()) {
                String str = database.getResultSet().getString("username");
                database.closeDatabaseNow();
                return str;
            }
        } catch (Exception e) {
            System.err.println("User的getUserName 异常");
        }
        return "无名氏";
    }

    public static String getUserImage(String id) {

        Database database = new Database("gobangdatabase", "root", "SS111827jj!");
        try {
            String sql = "select * from gobanguser where id =" + id + ";";
            database.findDatabase(sql);
            while (database.getResultSet().next()) {
                String str = database.getResultSet().getString("image");
                database.closeDatabaseNow();
                return str;
            }
            database.closeDatabaseNow();
        } catch (Exception e) {
            System.err.println("getUserImg 异常");
        }
        return "src/UserData/img/1.jpg";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getImage() {
        return image;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
