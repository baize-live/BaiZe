package Model;

import Bean.Service;
import Bean.User;
import Bean.Vars;
import Controller.Controller;
import View.GameWindow;
import View.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MainModel {

    /* 与服务器的交互 */
    public static class CreateHome implements Runnable {
        Service service;

        public CreateHome() {
            try {
                service = new Service();
            } catch (Exception e) {
                System.err.println("Create 初始化异常");
            }
        }

        @Override
        public void run() {
            try {
                // 发送
                service.sendInformation("2");
                service.sendInformation(User.getInstance().getId());
                // 接收
                if (service.receiveInformation().equals("1")) {
                    String homeNum = service.receiveInformation();
                    GameModel.getInstance().set(homeNum, "玩家");
                    ImageIcon image = new ImageIcon(User.getInstance().getImgPath());
                    image.setImage(image.getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));//可以用下面三句代码来代替
                    GameWindow.getUserPanelUserAImage().setIcon(image);

                    ImageIcon imageB = new ImageIcon(Vars.LocalData + "/img/9.jpg");
                    imageB.setImage(imageB.getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));//可以用下面三句代码来代替
                    GameWindow.getUserPanelUserBImage().setIcon(imageB);
                }
                service.closeStream();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("CreateHome 信息的异常");
            }
        }
    }

    public static class UpdateMainWindow implements Runnable {
        Service service;
        HashMap<String, JPanel> panelMap;

        public UpdateMainWindow() {
            panelMap = new HashMap<>();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    service = new Service();
                    service.sendInformation("11");
                    int num = Integer.parseInt(service.receiveInformation());

                    // 清除所有的按钮
                    for (String key : panelMap.keySet()) {
                        MainWindow.getInstance().getHomePanel().remove(panelMap.get(key));
                    }

                    // 添加新的按钮
                    for (int i = 0; i < num; i++) {
                        String homeNum = service.receiveInformation();
                        JPanel panel = new JPanel();
                        JButton homeButton = new JButton();
                        JLabel label = new JLabel();
                        JDialog dialog = new JDialog();
                        JButton inHome = new JButton("进入");
                        JButton watchGame = new JButton("观战");

                        inHome.addActionListener(e -> {
                            Controller.getInstance().inHome(homeNum);
                            dialog.setVisible(false);
                        });
                        watchGame.addActionListener(e -> {
                            Controller.getInstance().watchGame(homeNum);
                            dialog.setVisible(false);
                        });

                        dialog.setResizable(false);
                        dialog.setBackground(new Color(60, 60, 60, 255));
                        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
                        dialog.setSize(400, 110);

                        inHome.setPreferredSize(new Dimension(180, 50));
                        inHome.setForeground(Color.YELLOW);
                        inHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        inHome.setBackground(new Color(60, 60, 60, 255));
                        inHome.setFont(new Font("微软雅黑", Font.BOLD, 24));
                        inHome.setFocusable(false);

                        watchGame.setPreferredSize(new Dimension(180, 50));
                        watchGame.setForeground(Color.YELLOW);
                        watchGame.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        watchGame.setBackground(new Color(60, 60, 60, 255));
                        watchGame.setFont(new Font("微软雅黑", Font.BOLD, 24));
                        watchGame.setFocusable(false);

                        dialog.add(inHome);
                        dialog.add(watchGame);
                        dialog.setLocationRelativeTo(null);             //让窗口居中显示


                        homeButton.addActionListener(e -> dialog.setVisible(true));

                        ImageIcon image = new ImageIcon("src/localData/img/home.png");
                        image.setImage(image.getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));//可以用下面三句代码来代替
                        homeButton.setBorderPainted(false);
                        homeButton.setBackground(new Color(60, 60, 60, 255));
                        homeButton.setIcon(image);
                        homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                        label.setFont(new Font("Consolas", Font.BOLD, 24));
                        label.setForeground(Color.YELLOW);
                        label.setText(homeNum);
                        panel.setBackground(new Color(60, 60, 60, 255));
                        panel.add(label, BorderLayout.NORTH);
                        panel.add(homeButton, BorderLayout.CENTER);
                        panel.setPreferredSize(new Dimension(MainWindow.getInstance().getHomePanel().getWidth() / 3 - 10, MainWindow.getInstance().getHomePanel().getHeight() / 3));
                        panelMap.put(homeNum, panel);
                        MainWindow.getInstance().getHomePanel().add(panel);
                    }
                    MainWindow.getInstance().getHomePanel().updateUI();
                    service.closeStream();
                    Thread.sleep(200);
                } catch (Exception e) {
                    System.err.println("UpdateGameWindow 发送信息的异常");
                }
            }
        }
    }

    public static class InHome implements Runnable {
        Service service;
        String homeNum;

        public InHome(String homeNum) {
            try {

                service = new Service();
                this.homeNum = homeNum;
            } catch (Exception e) {
                System.err.println("InHome 初始化异常");
            }
        }

        @Override
        public void run() {
            try {
                service.sendInformation("3");
                service.sendInformation(User.getInstance().getId());
                service.sendInformation(homeNum);
                String bool = service.receiveInformation();
                if (bool.equals("1")) {
                    GameModel.getInstance().set(homeNum, "玩家");

                    ImageIcon image = new ImageIcon(User.getInstance().getImgPath());
                    image.setImage(image.getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));//可以用下面三句代码来代替
                    GameWindow.getUserPanelUserAImage().setIcon(image);

                    ImageIcon imageB = new ImageIcon(Vars.LocalData + "/img/9.jpg");
                    imageB.setImage(imageB.getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));//可以用下面三句代码来代替
                    GameWindow.getUserPanelUserBImage().setIcon(imageB);
                } else {
                    System.out.println("房间已满");
                }
                service.closeStream();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("InHome 发送信息的异常");
            }
        }
    }

    public static class WatchGame implements Runnable {
        Service service;
        String homeNum;

        public WatchGame(String homeNum) {
            try {
                service = new Service();
                this.homeNum = homeNum;
            } catch (Exception e) {
                System.err.println("WatchGame 初始化异常");
            }
        }

        @Override
        public void run() {
            try {
                service.sendInformation("5");
                service.sendInformation(User.getInstance().getId());
                service.sendInformation(homeNum);
                String bool = service.receiveInformation();

                if (bool.equals("1")) {
                    GameModel.getInstance().set(homeNum, "观战");
                    GameWindow.getUserPanelUserPrepareA().setEnabled(false);
                    GameWindow.getUserPanelUserPrepareB().setEnabled(false);
                    ImageIcon image = new ImageIcon(Vars.LocalData + "/img/9.jpg");
                    image.setImage(image.getImage().getScaledInstance(180, 180, Image.SCALE_DEFAULT));//可以用下面三句代码来代替
                    GameWindow.getUserPanelUserAImage().setIcon(image);
                    GameWindow.getUserPanelUserBImage().setIcon(image);
                    GameWindow.getUserPanelUserPrepareA().setVisible(false);
                    GameWindow.getUserPanelUserPrepareB().setVisible(false);
                } else {
                    /*
                     * */
                    System.out.println("观众席已满");
                }
                service.closeStream();
            } catch (Exception e) {
                System.err.println("WatchGame 发送信息的异常");
            }
        }
    }
}
