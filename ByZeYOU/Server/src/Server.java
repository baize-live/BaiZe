import service.Housekeeper;
import service.Login;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final Integer PORT;
    private Boolean bool;

    public Server() {
        bool = true;
        PORT = 3000;
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("服务器成功启动....");
            while (bool) {
                Socket client = serverSocket.accept();


                DataInputStream in = new DataInputStream(client.getInputStream());

                String serverNum;
                try {
                    serverNum = in.readUTF();
                } catch (Exception exception) {
                    exception.printStackTrace();
                    continue;
                }
                //做信号的分发
                switch (serverNum) {
                    case "1":
                        System.out.println(1);
                        new Thread(new Login(client)).start();
                        break;
                    case "2":
                        System.out.println(2);
                        new Thread(new Housekeeper.CreateHome(client)).start();
                        break;
                    case "3":
                        System.out.println(3);
                        new Thread(new Housekeeper.InHome(client)).start();
                        break;
                    case "4":
                        System.out.println(4);
                        new Thread(new Housekeeper.OutHome(client)).start();
                        break;
                    case "5":
                        System.out.println(5);
                        new Thread(new Housekeeper.WatchGame(client)).start();
                        break;
                    case "6":
                        System.out.println(6);
                        new Thread(new Housekeeper.Prepare(client)).start();
                        break;
                    case "7":
                        System.out.println(7);
                        new Thread(new Housekeeper.PlayChess(client)).start();
                        break;
                    case "8":
                        System.out.println(8);
                        new Thread(new Housekeeper.BackChess(client)).start();
                        break;
                    case "9":
                        System.out.println(9);
                        new Thread(new Housekeeper.Dialogue(client)).start();
                        break;
                    case "10":
                        System.out.println(10);
                        break;
                    case "11":
                        new Thread(new Housekeeper.UpdateGameWindow(client)).start();
                        break;
                    case "12":
                        new Thread(new Housekeeper.UpdateHome(client)).start();
                        break;
                    case "13":
                        new Thread(new Housekeeper.UpdateWatchHome(client)).start();
                        break;
                }
                if (Thread.currentThread().isInterrupted()) break;
            }
        } catch (Exception e) {
            System.err.println("Server 出现异常");
        }
    }

    public void stopServer() {
        bool = false;
    }

    @Override
    public void run() {
        startServer();
    }
}

