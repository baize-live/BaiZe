package Model;

import Bean.PlayMusic;
import Bean.Service;
import Bean.User;
import Bean.Vars;
import View.GameWindow;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GameModel {

    private static final GameModel instance = new GameModel();
    public final int BLACK = 1;
    public final int WHITE = -1;
    public final int SPACE = 0;
    public final int WIDTH = 18;
    public boolean userAState = false;
    public boolean userBState = false;
    public int[][] map = new int[WIDTH + 1][WIDTH + 1];
    private int time = 1;

    private int color;
    private String homeNum;
    private boolean turnWho;
    private boolean isStart;

    private GameModel() {
        color = SPACE;
        turnWho = false;
    }

    public static GameModel getInstance() {
        return instance;
    }

    public void set(String homeNum, String identity) {
        this.homeNum = homeNum;
        User.getInstance().setIdentity(identity);
        GameWindow.showWindow();
        time = 1;
        for (int i = 0; i <= WIDTH; i++) {
            for (int j = 0; j <= WIDTH; j++) {
                map[i][j] = SPACE;
            }
        }

        if (identity.equals("玩家")) {
            new Thread(new UpdateUserGameWindow()).start();
            UpdateUserGameWindow.startUpdate();
        }
        if (identity.equals("观战")) {
            new Thread(new UpdateWatchGameWindow()).start();
            UpdateWatchGameWindow.startUpdate();
        }
    }

    public void Reset() {
        color = SPACE;
        turnWho = false;
        userAState = false;
        GameWindow.getUserPanelUserPrepareA().setEnabled(true);
        GameWindow.getUserPanelUserPrepareA().setText("准备");

        if (User.getInstance().getIdentity().equals("玩家")) UpdateUserGameWindow.breakUpdate();
        if (User.getInstance().getIdentity().equals("观战")) UpdateWatchGameWindow.breakUpdate();
    }

    void print() {
        for (int i = 0; i <= WIDTH; i++) {
            for (int j = 0; j <= WIDTH; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    boolean isSuccess(int row, int col) {
        int num;
        // ——
        {
            num = 0;
            for (int i = col; i >= 0; i--) {
                if (map[row][i] == color) num++;
                else break;
            }
            for (int i = col; i <= WIDTH; i++) {
                if (map[row][i] == color) num++;
                else break;
            }
            if (num > 5) {
                return true;
            }
        }
        // |
        {
            num = 0;
            for (int i = row; i >= 0; i--) {
                if (map[i][col] == color) num++;
                else break;
            }
            for (int i = row; i <= WIDTH; i++) {
                if (map[i][col] == color) num++;
                else break;
            }
            if (num > 5) {
                return true;
            }
        }
        // \
        {
            num = 0;
            for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
                if (map[i][j] == color) num++;
                else break;
            }
            for (int i = row, j = col; i <= WIDTH && j <= WIDTH; i++, j++) {
                if (map[i][j] == color) num++;
                else break;
            }
            if (num > 5) {
                return true;
            }
        }
        // /
        {
            num = 0;
            for (int i = row, j = col; i >= 0 && j <= WIDTH; i--, j++) {
                if (map[i][j] == color) num++;
                else break;
            }
            for (int i = row, j = col; i <= WIDTH && j >= 0; i++, j--) {
                if (map[i][j] == color) num++;
                else break;
            }
            return num > 5;
        }
    }

    public boolean getIsStart() {
        return isStart;
    }

    public boolean getTurnWho() {
        return turnWho;
    }

    public interface UpdateGameWindow {
        void updateInformation() throws IOException;

        void updateUser() throws IOException;

        void updateDialog() throws IOException;

        void updateAudiences() throws IOException;

        void updateGame() throws IOException;
    }

    public static class SendDialog implements Runnable {
        Service service;
        String date;
        String text;

        public SendDialog(String date, String text) {
            try {
                service = new Service();
                this.date = date;
                this.text = text;
            } catch (Exception e) {
                System.err.println("SendDialog 初始化异常");
            }
        }

        @Override
        public void run() {
            try {
                service.sendInformation("9");
                service.sendInformation(User.getInstance().getId());
                service.sendInformation(GameModel.getInstance().homeNum);
                service.sendInformation(date);
                service.sendInformation(text);
                service.closeStream();

            } catch (Exception e) {
                System.err.println("SendDialog 发送信息的异常");
            }
        }
    }

    public static class OutHome implements Runnable {
        Service service;

        public OutHome() {
            try {
                service = new Service();
            } catch (Exception e) {
                System.err.println("OutHome 初始化异常");
            }
        }

        @Override
        public void run() {
            try {
                service.sendInformation("4");
                service.sendInformation(User.getInstance().getId());
                service.sendInformation(GameModel.getInstance().homeNum);
                GameModel.getInstance().Reset();
                service.closeStream();
            } catch (Exception e) {
                System.err.println("OutHome 发送信息的异常");
            }
        }
    }

    public static class Prepare implements Runnable {
        Service service;

        public Prepare() {
            try {
                service = new Service();
            } catch (Exception e) {
                System.err.println("Prepare 初始化异常");
            }
        }

        @Override
        public void run() {
            try {
                service.sendInformation("6");
                service.sendInformation(User.getInstance().getId());
                service.sendInformation(instance.homeNum);
                service.sendInformation(instance.userAState ? "1" : "0");
                service.closeStream();
            } catch (Exception e) {
                System.err.println("Prepare 发送信息的异常");
            }
        }
    }

    public static class PlayChess implements Runnable {
        Service service;
        int row;
        int col;

        public PlayChess(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void run() {
            try {
                if (!GameModel.getInstance().turnWho) {
                    System.out.println("不是您的回合");
                    return;
                }
                if (GameModel.getInstance().map[row][col] != GameModel.getInstance().SPACE) {
                    System.out.println("该位置有棋子");
                    return;
                }

                new Thread(new PlayMusic(Vars.LocalData + "music/music.wav", false, 0)).start();

                GameModel.getInstance().turnWho = false;
                GameModel.getInstance().map[row][col] = GameModel.getInstance().color;

                service = new Service();
                service.sendInformation("7");
                service.sendInformation(User.getInstance().getId());
                service.sendInformation(GameModel.getInstance().homeNum);
                service.sendInformation(Integer.toString(row));
                service.sendInformation(Integer.toString(col));
                if (GameModel.getInstance().isSuccess(row, col)) {
                    service.sendInformation("1");
                    GameModel.getInstance().turnWho = false;
                    GameModel.getInstance().userAState = false;
                    GameModel.getInstance().userBState = false;
                    new Thread(new PlayMusic(Vars.LocalData + "music/胜利.wav", false, 0)).start();
                    GameWindow.getDialog("恭喜你, 夺得胜利");
                } else {
                    service.sendInformation("0");
                }
                service.closeStream();
            } catch (Exception e) {
                System.err.println("PlayChess 异常");
            }
        }
    }

    public static class BackChess implements Runnable {

        Service service;

        public BackChess() {

        }

        @Override
        public void run() {
            try {
                service = new Service();
                service.sendInformation("8");
                service.sendInformation(User.getInstance().getId());
                service.sendInformation(GameModel.getInstance().homeNum);

                service.closeStream();
            } catch (Exception e) {
                System.err.println("BackChess 异常");
            }

        }
    }

    public static class UpdateUserGameWindow implements Runnable, UpdateGameWindow {
        static boolean isUpdate;  // 用于中断线程
        Service service;
        int time = 1;
        String name;

        public UpdateUserGameWindow() {
            isUpdate = false;
        }

        public static void startUpdate() {
            isUpdate = true;
        }

        public static void breakUpdate() {
            isUpdate = false;
        }

        @Override
        public void updateInformation() throws IOException {
            String str = service.receiveInformation();
            GameWindow.getInformationPanelLabel().setText(str);

            if (str.equals("请您下棋")) {
                GameModel.getInstance().isStart = true;
                if (time-- == 1) {
                    GameModel.getInstance().turnWho = true;
                    GameModel.getInstance().color = GameModel.getInstance().BLACK; // 设为黑棋
                    GameWindow.getUserPanelUserPrepareA().setEnabled(false);
                }
            } else if (str.equals("等待对方下棋")) {
                GameModel.getInstance().isStart = true;
                if (time-- == 1) {
                    GameModel.getInstance().turnWho = false;
                    GameModel.getInstance().color = GameModel.getInstance().WHITE; // 设为白棋
                    GameWindow.getUserPanelUserPrepareA().setEnabled(false);
                }
            }
        }

        @Override
        public void updateUser() throws IOException {
            String userAName = service.receiveInformation();  // 接收 用户A名字
            GameWindow.getUserPanelUserAName().setText(userAName);
            String userBName = service.receiveInformation();

            if (!userBName.equals("暂无用户")) {
                String[] contents = userBName.split(" ");
                GameWindow.getUserPanelUserBName().setText(contents[0]);
                name = contents[0];
                if (contents[1].equals("1")) {
                    GameWindow.getUserPanelUserPrepareB().setText("准备好了");
                    GameModel.instance.userBState = true;
                } else {
                    GameWindow.getUserPanelUserPrepareB().setText("没准备好");
                    GameModel.instance.userBState = false;
                }
            } else {
                GameWindow.getUserPanelUserBName().setText("暂无用户");
                GameWindow.getUserPanelUserPrepareB().setText("没准备好");
            }
        }

        @Override
        public void updateDialog() throws IOException {
            if (!service.receiveInformation().equals("0")) {
                GameWindow.getDialogPanelTextArea1().append(service.receiveInformation() + "   " + service.receiveInformation() + ": \n");
                String text = service.receiveInformation();
                GameWindow.getDialogPanelTextArea1().append(text + " \n ");
                try {
                    new Thread(new PlayMusic(Vars.LocalData + "music/" + text + ".wav", false, 0)).start();
                } catch (Exception exception) {
                    System.out.println("快捷语音 播放异常");
                }
            }
        }

        @Override
        public void updateAudiences() throws IOException {

        }

        @Override
        public void updateGame() throws IOException {
            if (service.receiveInformation().equals("0")) return;
            String[] contents = service.receiveInformation().split(" ");
            int row = Integer.parseInt(contents[0]);
            int col = Integer.parseInt(contents[1]);

            if (GameModel.getInstance().map[row][col] == GameModel.getInstance().SPACE) {
                try {
                    new Thread(new PlayMusic(Vars.LocalData + "music/music.wav", false, 0)).start();
                } catch (Exception e) {
                    System.out.println("声音播放异常");
                }
                GameModel.getInstance().map[row][col] = -1 * GameModel.getInstance().color;
                GameWindow.getInstance().repaint();
                if (contents[2].equals("0")) {
                    GameModel.getInstance().turnWho = true;
                } else {
                    GameModel.getInstance().turnWho = false;
                    GameModel.getInstance().userAState = false;
                    GameModel.getInstance().userBState = false;
                    if (GameModel.getInstance().time-- > 0 && Objects.equals(name, contents[2])) {
                        new Thread(new PlayMusic(Vars.LocalData + "music/失败.wav", false, 0)).start();
                        GameWindow.getDialog("很遗憾, " + name + "赢了");
                    }
                }
            }
        }

        @Override
        public void run() {
            while (isUpdate) {
                try {
                    service = new Service();
                    service.sendInformation("12");
                    service.sendInformation(User.getInstance().getId());
                    service.sendInformation(GameModel.getInstance().homeNum);

                    updateInformation();
                    updateUser();
                    updateDialog();
                    GameWindow.getInstance().repaint();
                    updateGame();
                    service.closeStream();
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("UpdateHome 发送信息的异常");
                }
            }
        }
    }

    public static class UpdateWatchGameWindow implements Runnable, UpdateGameWindow {
        static boolean isUpdate;  // 用于中断线程
        Service service;
        int ChessNum = 0;

        public UpdateWatchGameWindow() {
            isUpdate = false;
        }

        public static void startUpdate() {
            isUpdate = true;
        }

        public static void breakUpdate() {
            isUpdate = false;
        }

        @Override
        public void updateInformation() throws IOException {
            String str = service.receiveInformation();
            GameWindow.getInformationPanelLabel().setText(str);
            GameModel.getInstance().turnWho = false;
        }

        @Override
        public void updateUser() throws IOException {
            String userAName = service.receiveInformation();  // 接收 用户A名字
            GameWindow.getUserPanelUserAName().setText(userAName);
            String userBName = service.receiveInformation();
            GameWindow.getUserPanelUserBName().setText(userBName);
        }

        @Override
        public void updateDialog() throws IOException {

        }

        @Override
        public void updateAudiences() throws IOException {

        }

        @Override
        public void updateGame() throws IOException {
            if (service.receiveInformation().equals("0")) return;
            ChessNum++;
            String[] contents = service.receiveInformation().split(" ");
            int row = Integer.parseInt(contents[0]);
            int col = Integer.parseInt(contents[1]);
            GameModel.getInstance().map[row][col] = -1 * GameModel.getInstance().color;
            if (!contents[2].equals("0")) {
                GameModel.getInstance().color = GameModel.getInstance().SPACE;
                GameModel.getInstance().turnWho = false;
                GameModel.getInstance().userAState = false;
                GameModel.getInstance().userBState = false;
                GameWindow.getUserPanelUserPrepareA().setEnabled(true);
                GameWindow.getUserPanelUserPrepareA().setText("准备");
                if (GameModel.getInstance().time-- > 0) GameWindow.getDialog("很遗憾, 你失败了");
            }
        }

        @Override
        public void run() {
            while (isUpdate) {
                try {
                    service = new Service();
                    service.sendInformation("13");
                    service.sendInformation(User.getInstance().getId());
                    service.sendInformation(GameModel.getInstance().homeNum);
                    service.sendInformation(String.valueOf(ChessNum));

                    updateInformation();
                    updateUser();
//                    updateGame();
                    service.closeStream();
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("UpdateHome 发送信息的异常");
                }
            }
        }
    }

}
