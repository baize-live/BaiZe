package View;

import Bean.User;
import Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private static final MainWindow instance = new MainWindow();

    private static class Var {
        static final int WIDTH = 1280;
        static final int HEIGHT = 880;
        static final JButton createHomeButton = new JButton();
        static final JButton backLogButton = new JButton();
        static final JLabel name = new JLabel("Game Hall");
        static final JLabel welcome = new JLabel("Welcome");
        static final JLabel userName = new JLabel(User.getInstance().getName());
        static final JLabel image = new JLabel();
        static final JPanel namePanel = new JPanel();
        static final JPanel homePanel = new JPanel();
        static final JPanel welcomePanel = new JPanel();
        static final JPanel createPanel = new JPanel();
    }

    // 初始化
    private void PanelInit() {
        Var.createHomeButton.setText("CreateHome");
        Var.createHomeButton.setForeground(Color.YELLOW);
        Var.createHomeButton.setSize(200, 200);
        Var.createHomeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Var.createHomeButton.setBackground(new Color(60, 60, 60, 255));
        Var.createHomeButton.setFont(new Font("Consolas", Font.BOLD, 24));
        Var.createHomeButton.setFocusable(false);

        Var.backLogButton.setText("Log out");
        Var.backLogButton.setForeground(Color.YELLOW);
        Var.backLogButton.setSize(200, 200);
        Var.backLogButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Var.backLogButton.setBackground(new Color(60, 60, 60, 255));
        Var.backLogButton.setFont(new Font("Consolas", Font.BOLD, 24));
        Var.backLogButton.setFocusable(false);


        Var.name.setFont(new Font("Consolas", Font.BOLD, 80));
        Var.name.setForeground(Color.YELLOW);
        Var.namePanel.setLayout(new FlowLayout());
        Var.namePanel.setBackground(new Color(65, 60, 60, 255));
        Var.namePanel.add(Var.name);

        Var.homePanel.setLayout(new FlowLayout());
        Var.homePanel.setBackground(new Color(60, 60, 60, 255));

        Var.welcomePanel.setLayout(null);
        Var.welcomePanel.setBackground(new Color(60, 60, 65, 255));
        Var.welcome.setForeground(Color.YELLOW);
        Var.welcome.setFont(new Font("Consolas", Font.BOLD, 24));
        Var.userName.setForeground(Color.YELLOW);
        Var.userName.setFont(new Font("微软雅黑", Font.BOLD, 24));

        ImageIcon image = new ImageIcon(User.getInstance().getImgPath());
        image.setImage(image.getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));//可以用下面三句代码来代替
        Var.image.setIcon(image);
        Var.image.setBackground(Color.YELLOW);

        Var.welcome.setBounds(20, 80, 180, 50);
        Var.userName.setBounds(20, 150, 180, 50);
        Var.image.setBounds(20, 210, 180, 180);


        Var.welcomePanel.add(Var.welcome);
        Var.welcomePanel.add(Var.userName);
        Var.welcomePanel.add(Var.image);

        Var.createPanel.setLayout(null);
        Var.createPanel.setBackground(new Color(60, 60, 65, 255));
        Var.createHomeButton.setBounds(20, 80, 180, 50);
        Var.backLogButton.setBounds(20, 150, 180, 50);
        Var.createPanel.add(Var.createHomeButton);
        Var.createPanel.add(Var.backLogButton);

    }

    private void init() {
        this.setSize(Var.WIDTH, Var.HEIGHT);

        this.setResizable(false);
        //窗口名称
        this.setTitle("五子棋");
        //窗口默认布局
        this.setLayout(new BorderLayout());
        //设置窗口位置
        this.setLocationRelativeTo(null);             //让窗口居中显示
        //实现窗口关闭功能
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int rate_height = 7;
        int rate_width = 6;
        Var.namePanel.setPreferredSize(new Dimension(Var.WIDTH, Var.HEIGHT / rate_height));
        Var.homePanel.setPreferredSize(new Dimension(Var.WIDTH / rate_width * (rate_width - 2), Var.HEIGHT / rate_height * (rate_height - 1)));
        Var.createPanel.setPreferredSize(new Dimension(Var.WIDTH / rate_width, Var.HEIGHT / rate_height * (rate_height - 1)));
        Var.welcomePanel.setPreferredSize(new Dimension(Var.WIDTH / rate_width, Var.HEIGHT / rate_height * (rate_height - 1)));

        this.add(Var.namePanel, BorderLayout.NORTH);
        this.add(Var.homePanel, BorderLayout.CENTER);
        this.add(Var.welcomePanel, BorderLayout.WEST);
        this.add(Var.createPanel, BorderLayout.EAST);
    }

    // 业务
    private void service() {
        Var.createHomeButton.addActionListener(e -> Controller.getInstance().createHome());
        Controller.getInstance().updateMainWindow();
    }

    // 构造函数
    private MainWindow() {
        //先初始化 保证组件加载完成
        PanelInit();
        init();
        //设置可视
        this.setVisible(true);
        //最后走业务流程
        service();
    }

    //======接口======//
    public static MainWindow getInstance() {
        return instance;
    }

    public static void showWindow() {
        instance.setVisible(true);
        GameWindow.getInstance().setVisible(false);
    }

    public JPanel getHomePanel() {
        return Var.homePanel;
    }

}