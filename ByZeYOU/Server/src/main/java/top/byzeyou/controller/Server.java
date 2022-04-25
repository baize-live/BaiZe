package top.byzeyou.controller;

import org.apache.commons.io.IOUtils;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final Boolean BOOL = true;
    private static final Integer PORT = 2000;

    private static void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            // 做信号的分发
            while (BOOL) {
                Socket client = serverSocket.accept();
//                String serverNum = IOUtils.readLines(client.getInputStream(), "UTF-8");

                DataInputStream in = new DataInputStream(client.getInputStream());
                String serverNum = in.readUTF();
                //做信号的分发
                switch (serverNum) {
//                    case "1": new Thread(new Login(client)).start(); break;
//                    case "2": new Thread(new Download(client)).start();break;
//                    case "3": new Thread(new Game(client)).start();break;
                }
            }
        } catch (Exception e) {
            System.out.println("信号分发异常");
        }
    }
}
