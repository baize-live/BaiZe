package Controller;

import Model.GameModel;
import Model.LoginModel;
import Model.MainModel;
import View.LoginWindow;
import View.MainWindow;

public class Controller {
    private static final Controller instance = new Controller();

    public static Controller getInstance() {
        return instance;
    }

    public void login(String id, String password) {
        String stateCode = LoginModel.getInstance().Login(id, password);
        switch (stateCode) {
            case "0":
                System.out.println("登录失败");
                break;
            case "1":
                System.out.println("登录成功");
                LoginWindow.savePassword();
                LoginWindow.getInstance().setVisible(false);
                MainWindow.getInstance();
                break;
        }

    }

    public void createHome() {
        new Thread(new MainModel.CreateHome()).start();
    }

    public void inHome(String homeNum) {
        new Thread(new MainModel.InHome(homeNum)).start();
    }

    public void watchGame(String homeNum) {
        new Thread(new MainModel.WatchGame(homeNum)).start();
    }

    public void outHome() {
        new Thread(new GameModel.OutHome()).start();
    }

    public void prepare() {
        new Thread(new GameModel.Prepare()).start();
    }

    public void putChess(int row, int col) {
        new Thread(new GameModel.PlayChess(row, col)).start();
    }

    public void backChess() {
        new Thread(new GameModel.BackChess()).start();
    }

    public void updateMainWindow() {
        new Thread(new MainModel.UpdateMainWindow()).start();
    }

    // 发送对话
    public void sendDialog(String date, String text) {
        new Thread(new GameModel.SendDialog(date, text)).start();
    }

}
