package Model;

import Bean.Service;
import Bean.User;
import Bean.Vars;

public class LoginModel {
    private static final LoginModel instance = new LoginModel();

    public String Login(String id, String password) {
        // 发送消息
        Service service = null;
        String stateCode = "0";
        try {
            service = new Service();
            // 发送信息
            service.sendInformation("1");
            service.sendInformation(id);
            service.sendInformation(password);
            // 接收信息
            stateCode = service.receiveInformation();

            // 创建一个全局的角色
            if (stateCode.equals("1")) {
                User.createUser(id);
                User.getInstance().setName(service.receiveInformation());
                String file = Vars.LocalData + "img/" + User.getInstance().getId() + ".jpg";
                service.receiveFile(file);
                User.getInstance().setImgPath(file);
            }

            service.closeStream();
        } catch (Exception e) {
            System.err.println("LoginModel 异常");
        }
        return stateCode;
    }

    public static LoginModel getInstance() {
        return instance;
    }
}

