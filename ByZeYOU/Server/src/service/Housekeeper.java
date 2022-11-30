package service;

import bean.Home;
import bean.User;
import lookup.Service;

import java.io.IOException;
import java.net.Socket;
import java.util.*;

/// 管家
public class Housekeeper {
    private static final Integer MAX_HOME_SUM = 9; // 房间最大数
    private static final Housekeeper Interface = new Housekeeper();
    Integer homeNum;  // 房间编号
    Integer homeSum;  // 房间总数
    HashMap<Integer, Home> HomeMap; // 房间编号 ======> 房间  映射
    Queue<String> InformationQueue; // 消息队列

    private Housekeeper() {
        homeNum = 0;
        homeSum = 0;
        HomeMap = new HashMap<>();
    }

    private static String createHome(User user) {
        /*
         * 0 房间满了 不可以创建
         * 1 房间未满 可以创建
         * */
        if (Interface.homeSum++ < MAX_HOME_SUM) {
            Interface.HomeMap.put(++Interface.homeNum, new Home(user));
            return "1";
        }
        return "0";
    }

    private static String inHome(User user, String homeNum) {
        Home home = Interface.HomeMap.get(Integer.valueOf(homeNum));
        return home.inHome(user);
    }

    private static void outHome(User user, String homeNum) {
        Home home = Interface.HomeMap.get(Integer.valueOf(homeNum));
        home.outHome(user);
        if (home.getUserNum() == 0) {
            --Interface.homeSum;
            Interface.HomeMap.remove(Integer.valueOf(homeNum));
        }
    }

    private static String watchGame(User user, String homeNum) {
        Home home = Interface.HomeMap.get(Integer.valueOf(homeNum));
        return home.watchGame(user);
    }

    private static void prepare(User user, String homeNum, String state) {
        Home home = Interface.HomeMap.get(Integer.valueOf(homeNum));
        home.prepare(user, state);
    }

    private static void playChess(User user, String homeNum, String row, String col, String isSuccess) {
        Home home = Interface.HomeMap.get(Integer.valueOf(homeNum));
        home.playChess(user, row, col, isSuccess);
    }

    private static void backChess(User user, String homeNum) {
        Home home = Interface.HomeMap.get(Integer.valueOf(homeNum));
        home.backChess(user);
    }

    private static void dialogue(User user, String homeNum, String date, String text) {
        Home home = Interface.HomeMap.get(Integer.valueOf(homeNum));
        home.setDialogue(user, date, text);
    }

    public static Housekeeper getInterface() {
        return Interface;
    }

    //==================================================================//
    // 申请创建房子
    public static class CreateHome implements Runnable {
        private final Socket client;

        public CreateHome(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                Service service = new Service(client);
                String bool = createHome(new User(service.receiveInformation()));
                if (bool.equals("1")) {
                    // 返回 创建成功以及房间号
                    service.sendInformation(new ArrayList<>(Arrays.asList("1", Integer.toString(Interface.homeNum))));
                } else {
                    service.sendInformation(new ArrayList<>(Collections.singletonList("0")));
                }
                service.closeStream();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("CreateHome 的异常");
            }
        }
    }

    // 申请进入房子
    public static class InHome implements Runnable {
        private final Socket client;

        public InHome(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                Service service = new Service(client);
                String bool = inHome(new User(service.receiveInformation()), service.receiveInformation());
                if (bool.equals("1")) {
                    // 返回 进入成功
                    service.sendInformation(new ArrayList<>(Collections.singletonList("1")));
                } else {
                    service.sendInformation(new ArrayList<>(Collections.singletonList("0")));
                }
                service.closeStream();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("InHome 的异常");
            }
        }
    }

    // 告诉你出房子
    public static class OutHome implements Runnable {
        private final Socket client;

        public OutHome(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                Service service = new Service(client);
                outHome(new User(service.receiveInformation()), service.receiveInformation());
                service.closeStream();
            } catch (Exception e) {
                System.err.println("OutHome 的异常");
            }
        }
    }

    // 申请观战
    public static class WatchGame implements Runnable {
        private final Socket client;

        public WatchGame(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                Service service = new Service(client);
                String bool = watchGame(new User(service.receiveInformation()), service.receiveInformation());
                if (bool.equals("1")) {
                    // 返回 允许观战
                    service.sendInformation(new ArrayList<>(Collections.singletonList("1")));
                } else {
                    service.sendInformation(new ArrayList<>(Collections.singletonList("0")));
                }
                service.closeStream();
            } catch (Exception e) {
                System.err.println("WatchGame 的异常");
            }
        }
    }

    // 准备
    public static class Prepare implements Runnable {
        private final Socket client;

        public Prepare(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                Service service = new Service(client);
                prepare(new User(service.receiveInformation()), service.receiveInformation(), service.receiveInformation());
                service.closeStream();
            } catch (Exception e) {
                System.err.println("Prepare 的异常");
            }
        }
    }

    // 下棋
    public static class PlayChess implements Runnable {
        private final Socket client;

        public PlayChess(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                Service service = new Service(client);
                String id = service.receiveInformation();
                String homeNum = service.receiveInformation();
                String row = service.receiveInformation();
                String col = service.receiveInformation();
                String isSuccess = service.receiveInformation();

                playChess(new User(id), homeNum, row, col, isSuccess);
                service.closeStream();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("PlayChess 异常");
            }
        }
    }

    public static class BackChess implements Runnable {
        private final Socket client;

        public BackChess(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                Service service = new Service(client);
                String id = service.receiveInformation();
                String homeNum = service.receiveInformation();
                backChess(new User(id), homeNum);
                service.closeStream();
            } catch (Exception e) {
                System.err.println("PlayChess 异常");
            }
        }
    }

    // 得到对话
    public static class Dialogue implements Runnable {
        private final Socket client;

        public Dialogue(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                Service service = new Service(client);
                dialogue(new User(service.receiveInformation()), service.receiveInformation(), service.receiveInformation(), service.receiveInformation());
                service.closeStream();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Dialogue 的异常");
            }
        }
    }

    // 更新GameWindow
    public static class UpdateGameWindow implements Runnable {
        private final Socket client;

        public UpdateGameWindow(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                Service service = new Service(client);
                // 将内容存在contents中
                ArrayList<String> contents = new ArrayList<>();
                contents.add(Integer.toString(Housekeeper.getInterface().HomeMap.size()));
                for (Integer key : Housekeeper.getInterface().HomeMap.keySet()) {
                    contents.add(Integer.toString(key));
                }
                // 发送信息
                service.sendInformation(contents);
                service.closeStream();
            } catch (Exception e) {
                System.err.println("UpdateGameWindow 的异常");
            }
        }
    }

    // 更新Home
    public static class UpdateHome implements Runnable {
        private final Socket client;
        private Home home;
        private String id;
        private Service service;

        public UpdateHome(Socket client) {
            this.client = client;
            this.home = null;
            this.service = null;
        }

        private void updateInformation() throws IOException {
            service.sendInformation(new ArrayList<>(Collections.singletonList(home.getInformation(id))));
        }

        private void updateUserA() throws IOException {
            service.sendInformation(new ArrayList<>(Collections.singletonList(home.getUserA(id))));
        }

        private void updateUserB() throws IOException {
            service.sendInformation(new ArrayList<>(Collections.singletonList(home.getUserB(id))));
        }

        private void updateGame() throws IOException {
            String str = home.getGame(id);
            if (str.equals("0")) {
                service.sendInformation(new ArrayList<>(Collections.singletonList("0")));
            } else {
                service.sendInformation(new ArrayList<>(Arrays.asList("1", str)));
            }
        }

        private void updateDialog() throws IOException {
            String str = home.getDialogue(id);
            if (str.equals("0")) {
                service.sendInformation(new ArrayList<>(Collections.singletonList("0")));
            } else {
                String[] contents = str.split("!/@/#/@/!");
                service.sendInformation(new ArrayList<>(Arrays.asList("1", contents[0], contents[1], contents[2])));
            }
        }

        @Override
        public void run() {
            try {
                service = new Service(client);
                id = service.receiveInformation();
                home = Interface.HomeMap.get(Integer.valueOf(service.receiveInformation()));
                updateInformation();
                updateUserA();
                updateUserB();
                updateDialog();
                updateGame();
                service.closeStream();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("UpdateHome 的异常");
            }
        }
    }

    // 更新观察者Home
    public static class UpdateWatchHome implements Runnable {
        private final Socket client;
        private Home home;
        private String id;
        private Service service;
        private int ChessNum;

        public UpdateWatchHome(Socket client) {
            this.client = client;
            this.home = null;
            this.service = null;
            ChessNum = 0;
        }

        private void updateInformation() throws IOException {
            service.sendInformation(new ArrayList<>(Collections.singletonList("欢迎你  观战")));
        }

        private void updateUserA() throws IOException {
            service.sendInformation(new ArrayList<>(Collections.singletonList(home.getUserA())));
        }

        private void updateUserB() throws IOException {
            service.sendInformation(new ArrayList<>(Collections.singletonList(home.getUserB())));
        }

        private void updateGame(int num) throws IOException {
            String str = home.getGame(num);
            if (str.equals("0")) {
                service.sendInformation(new ArrayList<>(Collections.singletonList("0")));
            } else {
                service.sendInformation(new ArrayList<>(Arrays.asList("1", str)));
            }
        }

        @Override
        public void run() {
            try {
                service = new Service(client);
                id = service.receiveInformation();
                home = Interface.HomeMap.get(Integer.valueOf(service.receiveInformation()));
                int num = Integer.parseInt(service.receiveInformation());
                updateInformation();
                updateUserA();
                updateUserB();
                updateGame(num);
                service.closeStream();
            } catch (Exception e) {
                System.err.println("UpdateHome 的异常");
            }
        }

    }
}
