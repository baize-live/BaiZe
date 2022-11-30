package View;

import Bean.Vars;
import Bean.WordWork;
import Controller.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LoginWindow extends JFrame {

    //========================接口=================================//
    public static LoginWindow getInstance() {
        return instance;
    }

    //自动登录
    public static void automaticLog() {
        if (Components.automaticLog.isSelected()) {
            Components.logButton.doClick();
        }
    }

    //记住密码
    public static void savePassword() {
        String[] content;
        if (isSavaPassword()) {
            content = new String[]{
                    Components.usernameText.getText(),
                    Components.passwordText.getText(),
                    "1",//是否记住密码
                    (isAutomaticLog() ? "1" : "0"),//是否自动登录
            };
        } else {
            content = new String[]{
                    Components.usernameText.getText(),
                    "", //密码不保存
                    "0",//不记住密码
                    "0" //不自动登录
            };
        }
        ArrayList<String> contentList = new ArrayList<>();
        Collections.addAll(contentList, content);
        WordWork.writeTxt("src/localData/logData.txt", contentList, false);
    }
    //=============================================//

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        try {
            g.drawImage(ImageIO.read(new File("src/localData/img/login.png")), 0, 0, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final LoginWindow instance = new LoginWindow();

    private static class Components {
        static final int WIDTH = 880;
        static final int HEIGHT = 480;
        //获得初始化文本
        static JLabel background;
        static ArrayList<String> content = initContent();
        static JLabel username = new JLabel("账号:");
        static JLabel password = new JLabel("密码:");
        static JTextField usernameText = new JTextField(content.get(0));
        static JPasswordField passwordText = new JPasswordField(content.get(1));
        static JButton logButton = new JButton();
        static JCheckBox savePassword = new JCheckBox("记住密码", content.get(2).equals("1"));
        static JCheckBox automaticLog = new JCheckBox("自动登录", content.get(3).equals("1"));
        //申明一个对话框
        static JDialog dialog;
    }

    //初始化
    private void init() {
        this.setSize(Components.WIDTH, Components.HEIGHT);
        //不可以调整大小
        this.setResizable(false);
        //窗口名称
        this.setTitle("五子棋登录");
        //取消窗口默认布局
        this.setLayout(null);
        //设置窗口位置
        this.setLocationRelativeTo(null);             //让窗口居中显示
        //实现窗口关闭功能
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Font font = new Font("微软雅黑", Font.PLAIN, 20);
        Font font2 = new Font("微软雅黑", Font.PLAIN, 16);

        //声明一个对话框
        Components.dialog = new JDialog(this, true);
        //组件布局
        Components.username.setBounds(Components.WIDTH / 3 * 2, Components.HEIGHT / 3 - 25, 80, 30);
        Components.password.setBounds(Components.WIDTH / 3 * 2, Components.HEIGHT / 3 + 25, 80, 30);
        Components.usernameText.setBounds(Components.WIDTH / 3 * 2 + 60, Components.HEIGHT / 3 - 25, 130, 30);
        Components.passwordText.setBounds(Components.WIDTH / 3 * 2 + 60, Components.HEIGHT / 3 + 25, 130, 30);
        Components.savePassword.setBounds(Components.WIDTH / 3 * 2, Components.HEIGHT / 3 + 80, 90, 40);
        Components.automaticLog.setBounds(Components.WIDTH / 3 * 2, Components.HEIGHT / 3 + 125, 90, 40);
        Components.logButton.setBounds(Components.WIDTH / 3 * 2 + 20, Components.HEIGHT / 3 + 175, 160, 40);

        //窗口添加组件
        Components.username.setFont(font);
        Components.password.setFont(font);
        Components.usernameText.setFont(font);
        Components.passwordText.setFont(font);
        Components.savePassword.setFont(font2);
        Components.automaticLog.setFont(font2);
        Components.logButton.setFont(font);
        Components.savePassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Components.automaticLog.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Components.logButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Components.logButton.setIcon(new ImageIcon("src/localData/img/log.png"));
        this.add(Components.username);
        this.add(Components.password);
        this.add(Components.usernameText);
        this.add(Components.passwordText);
        this.add(Components.logButton);
        this.add(Components.savePassword);
        this.add(Components.automaticLog);
    }

    //业务
    private void service() {
        //===============================业务===============================//
        //登录
        Components.logButton.addActionListener(e -> Controller.getInstance().login(
                Components.usernameText.getText(),
                Components.passwordText.getText()));
        //记住密码  点击取消记住密码 自动取消自动登录
        Components.savePassword.addActionListener(e -> notSave());
        //自动登录  点击自动登录     自动记住密码
        Components.automaticLog.addActionListener(e -> autoLog());
        //===============================业务===============================//
    }

    //构造函数
    private LoginWindow() {
        //先初始化 保证组件加载完成
        init();
        //设置可视
        this.setVisible(true);
        //最后走业务流程
        service();
    }

    //返回是否记住密码
    private static boolean isSavaPassword() {
        return Components.savePassword.isSelected();
    }

    //设置是否记住密码
    private static void setSavePassword() {
        Components.savePassword.setSelected(true);
    }

    //返回是否自动登录
    private static boolean isAutomaticLog() {
        return Components.automaticLog.isSelected();
    }

    //设置是否自动登录
    public static void setAutomaticLog(boolean bool) {
        Components.automaticLog.setSelected(bool);
    }

    //记住密码
    private static void notSave() {
        if (!isSavaPassword()) {
            setAutomaticLog(false);
        }
    }

    //自动登录
    private static void autoLog() {
        if (isAutomaticLog()) {
            setSavePassword();
        }
    }

    //初始化content
    private static ArrayList<String> initContent() {
        //接收返回值
        ArrayList<String> content = WordWork.readTxt(Vars.LocalData + "/logData.txt");
        //相当于赋初值
        if (content.size() == 0) content.add("");
        content.add("");
        content.add("0");
        content.add("0");
        return content;
    }

}
