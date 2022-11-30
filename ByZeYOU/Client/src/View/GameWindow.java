package View;

import Bean.PlayMusic;
import Bean.Vars;
import Controller.Controller;
import Model.GameModel;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Date;
import java.util.Objects;

public class GameWindow extends JFrame {
    // 实现单例
    private static final GameWindow instance = new GameWindow();

    private GameWindow() {
        //先初始化 保证组件加载完成
        init();
        //设置可视
        this.setVisible(true);
        //最后走业务流程
        //this.pack();
        service();
    }

    public static GameWindow getInstance() {
        return instance;
    }

    public static void showWindow() {
        instance.setVisible(true);
        MainWindow.getInstance().setVisible(false);
//        Components.updateHomeThread = new Thread(new Update.UpdateHome(instance.HomeNum));
//        Components.updateHomeThread.start();
    }

    // 主面板
    private static class GamePanel extends JPanel {
        void init() {
            this.setLayout(new BorderLayout());
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(Color.BLACK);

            int width = getWidth();
            int height = getHeight();
            int min = Math.min(width, height);
            int gvc = min / GameModel.getInstance().WIDTH;
            int startWidth = (width - gvc * GameModel.getInstance().WIDTH) / 2;
            int startHeight = (height - gvc * GameModel.getInstance().WIDTH) / 2;
            int r = gvc - 5;

            ImageIcon image = new ImageIcon(Vars.LocalData + "/img/Chess.jpg");
            image.setImage(image.getImage().getScaledInstance(this.getWidth(), this.getHeight(),
                    Image.SCALE_AREA_AVERAGING));//这里设置图片的大小和窗口的大小相等
            g.drawImage(image.getImage(), 0, 0, this);//在窗口上画出该图片


            for (int i = 0; i <= GameModel.getInstance().WIDTH; ++i) {
                g.drawLine(startWidth, startHeight + i * gvc, startWidth + gvc * GameModel.getInstance().WIDTH, startHeight + i * gvc);
                g.drawLine(startWidth + i * gvc, startHeight, startWidth + i * gvc, startHeight + gvc * GameModel.getInstance().WIDTH);
            }


            if (GameModel.getInstance().getIsStart()) {
                for (int i = 0; i <= GameModel.getInstance().WIDTH; i++) {
                    for (int j = 0; j <= GameModel.getInstance().WIDTH; j++) {
                        //抗锯齿
                        Graphics2D g2d = (Graphics2D) g;
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                        float[] fractions = new float[]{0f, 1f};
                        Color[] colors = new Color[]{};
                        int radius = 0;

                        if (GameModel.getInstance().map[i][j] == GameModel.getInstance().SPACE) continue;

                        int startWid = startWidth + j * gvc - r / 2;
                        int startHei = startHeight + i * gvc - r / 2;

                        if (GameModel.getInstance().map[i][j] == GameModel.getInstance().BLACK) {
                            colors = new Color[]{Color.BLACK, Color.GRAY};
                            radius = r * 4 / 3;
                        }
                        if (GameModel.getInstance().map[i][j] == GameModel.getInstance().WHITE) {
                            colors = new Color[]{Color.WHITE, Color.BLACK};
                            radius = r * 2;
                        }

                        RadialGradientPaint paint = new RadialGradientPaint(startWid, startHei, radius, fractions, colors);
                        ((Graphics2D) g).setPaint(paint);

                        g.fillOval(startWid, startHei, r, r);
                    }
                }
            }
        }

        GamePanel() {
            init();
            this.setVisible(true);
        }
    }

    // 对话区
    private static class DialogPanel extends JPanel {
        static JTextArea textArea1 = new JTextArea();
        static JScrollPane rollingTextArea1 = new JScrollPane(textArea1);
        static JTextArea textArea2 = new JTextArea();
        static JScrollPane rollingTextArea2 = new JScrollPane(textArea2);
        static JButton send = new JButton("发送");
        static JLabel label = new JLabel("聊天区");
        static JComboBox<String> comboBox = new JComboBox<String>();

        void myResize(int width, int height) {
            final int rate_width = 4;
            final int rate_height = 8;
            final int gvc = 5;

            label.setPreferredSize(new Dimension(
                    width - gvc, height / rate_height - gvc
            ));
            rollingTextArea1.setPreferredSize(new Dimension(
                    width - gvc, height / rate_height * (rate_height - 3) - gvc
            ));
            rollingTextArea2.setPreferredSize(new Dimension(
                    width / rate_width * (rate_width - 1) - gvc, height / rate_height - gvc
            ));
            send.setPreferredSize(new Dimension(
                    width / rate_width - gvc, height / rate_height - gvc
            ));
            comboBox.setPreferredSize(new Dimension(
                    width - gvc, height / rate_height / 3 * 2 - gvc
            ));
        }

        // 聊天区
        DialogPanel() {
            this.setLayout(new FlowLayout());
            this.setBackground(new Color(80, 80, 80, 255));
            textArea1.setEditable(false);
            textArea1.setFont(new Font("微软雅黑", Font.BOLD, 16));
            textArea2.setFont(new Font("微软雅黑", Font.BOLD, 16));

            label.setForeground(Color.YELLOW);
            label.setFont(new Font("微软雅黑", Font.BOLD, 32));

            send.setFont(new Font("微软雅黑", Font.BOLD, 16));
            send.setForeground(Color.YELLOW);
            send.setCursor(new Cursor(Cursor.HAND_CURSOR));
            send.setBackground(new Color(60, 60, 60, 255));
            send.setFocusable(false);

            comboBox.setFont(new Font("微软雅黑", Font.BOLD, 20));
            comboBox.setForeground(Color.YELLOW);
            comboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
            comboBox.setBackground(new Color(60, 60, 60, 255));
            comboBox.setFocusable(false);

            comboBox.addItem("快捷语");
            comboBox.addItem("搞快点 搞快点");
            comboBox.addItem("心态崩了呀");
            comboBox.addItem("就这？");
            comboBox.addItem("这谁顶得住啊");
            comboBox.addItem("顶不住也要顶");
            comboBox.addItem("快点啊，等到花儿都谢了");
            comboBox.addItem("你的棋打得也太好了");
            comboBox.addItem("你好，很高兴见到你");
            comboBox.addItem("不好意思，我要离开一会儿");

            this.add(label);
            this.add(rollingTextArea1);
            this.add(rollingTextArea2);
            this.add(send);
            this.add(comboBox);

            this.setVisible(true);

            this.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    myResize(getWidth(), getHeight());
                    repaint();
                }
            });
        }
    }

    // 用户面板
    private static class UserPanel extends JPanel {
        static JLabel userAImage = new JLabel();
        static JLabel userBImage = new JLabel();
        static JLabel userAName = new JLabel();
        static JLabel userBName = new JLabel();
        static JButton prepareA = new JButton("准备");
        static JButton prepareB = new JButton("准备");
        static JButton surrender = new JButton("投降");
        static JButton backChess = new JButton("悔棋");

        // 功能区
        UserPanel() {
            this.setLayout(new FlowLayout(FlowLayout.CENTER));
            this.setBackground(new Color(60, 60, 60, 255));

            userAName.setForeground(Color.YELLOW);
            userAName.setFont(new Font("微软雅黑", Font.BOLD, 24));
//            userAName.setPreferredSize(new Dimension(180,50));
            userBName.setForeground(Color.YELLOW);
            userBName.setFont(new Font("微软雅黑", Font.BOLD, 24));
//            userBName.setPreferredSize(new Dimension(180,50));

            prepareA.setPreferredSize(new Dimension(130, 50));
            prepareA.setForeground(Color.YELLOW);
            prepareA.setFont(new Font("微软雅黑", Font.BOLD, 24));
            prepareA.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            prepareA.setBackground(new Color(60, 60, 60, 255));
            prepareA.setFocusable(false);

            prepareB.setPreferredSize(new Dimension(130, 50));
            prepareB.setForeground(Color.YELLOW);
            prepareB.setFont(new Font("微软雅黑", Font.BOLD, 24));
            prepareB.setBackground(new Color(60, 60, 60, 255));
            prepareB.setFocusable(false);

            this.add(userAImage);
            this.add(userAName);
            this.add(prepareA);

            this.add(userBImage);
            this.add(userBName);
            this.add(prepareB);

            prepareB.setEnabled(false);

//            this.add(surrender);
//            this.add(backChess);

            this.setVisible(true);
        }
    }

    // 观众席
    private static class Audiences extends JPanel {
        // 观众席
        Audiences() {
            this.setLayout(new FlowLayout(FlowLayout.LEFT));

            JLabel label = new JLabel("观众席");
            label.setForeground(Color.YELLOW);
            label.setFont(new Font("微软雅黑", Font.BOLD, 24));

            this.add(label);
            this.setBackground(new Color(60, 60, 60, 255));
            this.setVisible(true);
        }
    }

    // 消息区
    private static class InformationPanel extends JPanel {
        static JButton closeButton = new JButton();
        static JLabel information = new JLabel("");

        // 信息区
        InformationPanel() {
            this.setLayout(new FlowLayout(FlowLayout.CENTER));
            this.setBackground(new Color(60, 60, 60, 255));

            closeButton.setText("离开房间");
            closeButton.setForeground(Color.YELLOW);
            closeButton.setSize(200, 80);
            closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            closeButton.setBackground(new Color(60, 60, 60, 255));
            closeButton.setFont(new Font("微软雅黑", Font.BOLD, 24));
            closeButton.setFocusable(false);

            information.setForeground(Color.YELLOW);
            information.setFont(new Font("微软雅黑", Font.BOLD, 24));
            this.add(information);
            this.add(closeButton);
            this.setVisible(true);
        }
    }

    // 组件库
    private static class Var {
        static final int WIDTH = 1280;
        static final int HEIGHT = 900;
        static final int rate_width = 7;
        static final int rate_height = 10;
        static GamePanel gamePanel = new GamePanel();
        static DialogPanel dialogPanel = new DialogPanel();
        static UserPanel userPanel = new UserPanel();
        static Audiences audiences = new Audiences();
        static InformationPanel informationPanel = new InformationPanel();
        static JDialog dialog = new JDialog(instance, true);
        static JLabel label = new JLabel();
    }

    // 重定大小
    void myResize(int width, int height) {
        Var.gamePanel.setPreferredSize(new Dimension(
                width / Var.rate_width * (Var.rate_width - 2), height / Var.rate_height * (Var.rate_height - 2)
        ));
        Var.informationPanel.setPreferredSize(new Dimension(
                width, height / Var.rate_height
        ));
        Var.audiences.setPreferredSize(new Dimension(
                width, height / Var.rate_height
        ));
        Var.userPanel.setPreferredSize(new Dimension(
                width / Var.rate_width, height / Var.rate_height * (Var.rate_height - 2)
        ));
        Var.dialogPanel.setPreferredSize(new Dimension(
                width / Var.rate_width * 2, height / Var.rate_height * (Var.rate_height - 2)
        ));
    }

    // 初始化
    private void init() {
//        this.setUndecorated(true);
        this.setMinimumSize(new Dimension(Var.WIDTH, Var.HEIGHT));
        this.setResizable(false);
        //窗口名称
        this.setTitle("五子棋");
        //窗口默认布局
        this.setLayout(new BorderLayout());
        //设置窗口位置
        this.setLocationRelativeTo(null);             //让窗口居中显示
        //窗口背景
        this.setBackground(new Color(80, 80, 80, 255));
        //
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                instance.setVisible(false);
                myResize(Var.WIDTH, Var.HEIGHT);
                DialogPanel.textArea1.setText("");
                DialogPanel.textArea2.setText("");
                MainWindow.showWindow();
                Controller.getInstance().outHome();
            }
        });

        myResize(Var.WIDTH, Var.HEIGHT);
        this.add(Var.informationPanel, BorderLayout.NORTH); //上
        this.add(Var.audiences, BorderLayout.SOUTH);        //下
        this.add(Var.gamePanel, BorderLayout.CENTER);       //中
        this.add(Var.userPanel, BorderLayout.WEST);         //左
        this.add(Var.dialogPanel, BorderLayout.EAST);       //右
        Var.label.setFont(new Font("微软雅黑", Font.BOLD, 24));
        Var.dialog.add(Var.label);
        Var.dialog.setResizable(false);
        Var.dialog.setSize(300, 100);
        Var.dialog.setLocationRelativeTo(null);             //让窗口居中显示

    }

    // 业务
    private void service() {
        // 重画大小
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();
                super.componentResized(e);
                myResize(width, height);
            }
        });

        // 离开房间
        InformationPanel.closeButton.addActionListener(e -> {
            instance.setVisible(false);
            myResize(Var.WIDTH, Var.HEIGHT);
            DialogPanel.textArea1.setText("");
            DialogPanel.textArea2.setText("");
            MainWindow.showWindow();
            Controller.getInstance().outHome();
        });

        UserPanel.prepareA.addActionListener(e -> {
            if (GameModel.getInstance().userAState) {
                UserPanel.prepareA.setText("准备");
                GameModel.getInstance().userAState = false;
            } else {
                UserPanel.prepareA.setText("取消准备");
                GameModel.getInstance().userAState = true;
            }
            Controller.getInstance().prepare();
        });

        UserPanel.backChess.addActionListener(e -> {
            Controller.getInstance().backChess();
        });

        DialogPanel.send.addActionListener(e -> {
            // 不发送空格
            if (!DialogPanel.textArea2.getText().equals("")) {
                Controller.getInstance().sendDialog(new Date() + "", DialogPanel.textArea2.getText());
                DialogPanel.textArea1.append(new Date() + " \n " + DialogPanel.textArea2.getText() + " \n ");
                DialogPanel.textArea2.setText("");
            }
        });

        DialogPanel.comboBox.addActionListener(e -> {
            String item = (String) DialogPanel.comboBox.getSelectedItem();
            if (!Objects.equals(item, "快捷语")) {
                DialogPanel.textArea2.setText(item);
                DialogPanel.send.doClick();
                DialogPanel.comboBox.setSelectedIndex(0);
                try {
                    new Thread(new PlayMusic(Vars.LocalData + "music/" + item + ".wav", false, 0)).start();
                } catch (Exception exception) {
                    //System.out.println("快捷语音 播放异常");
                }
            }
        });

        Var.gamePanel.addMouseListener(new MouseAdapter() {
            final int width = Var.gamePanel.getWidth();
            final int height = Var.gamePanel.getHeight();
            final int min = Math.min(width, height);
            final int gvc = min / GameModel.getInstance().WIDTH;
            final int startWidth = (width - gvc * GameModel.getInstance().WIDTH) / 2;
            final int startHeight = (height - gvc * GameModel.getInstance().WIDTH) / 2;

            private int getRow(MouseEvent e) {
                int row = (e.getY() - startHeight) / gvc;
                if ((e.getY() - startHeight) % gvc >= gvc / 2)
                    ++row;
                return row;
            }

            private int getCol(MouseEvent e) {
                int col = (e.getX() - startWidth) / gvc;
                if ((e.getX() - startWidth) % gvc >= gvc / 2)
                    ++col;
                return col;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int row = getRow(e);
                int col = getCol(e);
                repaint();
                Controller.getInstance().putChess(row, col);
            }
        });

    }

    //=============================接口========================================//

    public static void getDialog(String str) {
        Var.label.setText(str);
        Var.dialog.setVisible(true);
    }

    public static JLabel getInformationPanelLabel() {
        return InformationPanel.information;
    }

    public static JLabel getUserPanelUserAImage() {
        return UserPanel.userAImage;
    }

    public static JLabel getUserPanelUserBImage() {
        return UserPanel.userBImage;
    }

    public static JLabel getUserPanelUserAName() {
        return UserPanel.userAName;
    }

    public static JButton getUserPanelUserPrepareA() {
        return UserPanel.prepareA;
    }

    public static JLabel getUserPanelUserBName() {
        return UserPanel.userBName;
    }

    public static JButton getUserPanelUserPrepareB() {
        return UserPanel.prepareB;
    }

    public static JTextArea getDialogPanelTextArea1() {
        return DialogPanel.textArea1;
    }

}
