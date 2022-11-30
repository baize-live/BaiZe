package bean;

import java.util.ArrayList;

public class Global {
    public static final String localData = "src/UserData/";
    private static final Global Interface = new Global();
    private static final Integer MAX_TRAFFIC = 100;  // 最大流量
    public final ArrayList<User> UserList;                   // 存放客户id

    private Global() {
        UserList = new ArrayList<>();
    }

    /*
     * 心跳检测
     * */
    public static Global getInterface() {
        return Interface;
    }
}
