package Bean;

import java.io.*;
import java.net.Socket;

public class Service {
    // 客户端
    private Socket client = null;
    // 缓冲流
    private BufferedInputStream bis = null;
    // 缓冲流
    private BufferedOutputStream bos = null;
    // 输出流
    private final DataOutputStream output;
    // 输入流
    private final DataInputStream input;

    public Service() throws Exception {
        client = new Socket(Vars.address, 3000);
        output = new DataOutputStream(client.getOutputStream());
        input = new DataInputStream(client.getInputStream());
    }

    // 发送信息
    public void sendInformation(String content) throws IOException {
        output.writeUTF(content);
    }

    // 接收信息
    public String receiveInformation() throws IOException {
        return input.readUTF();
    }

    // 发送文件
    public void sendFile(String file) throws IOException {
        //获取源文件的输入流
        bis = new BufferedInputStream(new FileInputStream(file));
        //获取socket的输出流并包装
        bos = new BufferedOutputStream(client.getOutputStream());
        byte[] b = new byte[2048];
        int len;
        while ((len = bis.read(b)) != -1) bos.write(b, 0, len);
        bis.close();
        bos.close();
    }

    // 接收文件
    public void receiveFile(String file) throws IOException {
        //获取socket的输入流并包装
        bis = new BufferedInputStream(client.getInputStream());
        //获取目标文件的输出流
        bos = new BufferedOutputStream(new FileOutputStream(file));

        byte[] b = new byte[2048];
        int len;
        while ((len = bis.read(b)) != -1) bos.write(b, 0, len);
        bis.close();
        bos.close();
    }

    public void closeStream() {
        try {
            output.close();
            input.close();
            client.close();
            if (bis != null) bis.close();
            if (bos != null) bos.close();
        } catch (Exception e) {
            System.err.println("流 关闭异常");
        }
    }
}
