package service;

import bean.Global;
import bean.User;
import lookup.Database;
import lookup.Service;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;


public class Login implements Runnable {

    private final Socket client;

    public Login(Socket client) {
        this.client = client;
    }

    private String findUser(String id, String password) {
        /* @return 账号密码是否正确 */
        Database database = new Database("gobangdatabase", "root", "SS111827jj!");
        try {
            String sql = "select * from gobanguser where id =" + id + ";";
            database.findDatabase(sql);
            while (database.getResultSet().next()) {
                if (password.equals(database.getResultSet().getString("password"))) {
                    database.closeDatabaseNow();
                    return "1";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Login 的findUser方法异常");
        }
        database.closeDatabaseNow();
        return "0";
    }

    @Override
    public void run() {
        try {
            Service service = new Service(client);

            //接受客户端发来的账号和密码
            String id = service.receiveInformation();
            String password = service.receiveInformation();

            //经过处理发回去一个boolean
            String bool = findUser(id, password);

            if (Objects.equals(bool, "1")) {
                service.sendInformation(new ArrayList<String>(Arrays.asList("1", User.getUserName(id))));
                service.sendFile(User.getUserImage(id));
                Global.getInterface().UserList.add(new User(id));
            } else {
                service.sendInformation(new ArrayList<String>(Collections.singletonList("0")));
            }
            service.closeStream();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Login 类的异常");
        }
    }

}
