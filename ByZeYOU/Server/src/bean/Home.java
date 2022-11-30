package bean;

import java.util.*;

public class Home {
    final int AUDIENCE_MAX = 5; // 观众的数量
    int userNum;//玩家数量
    User userA; //玩家A
    User userB; //玩家B
    Stack<Row_Col> stackA; //玩家A的棋子
    Stack<Row_Col> stackB; //玩家B的棋子
    Stack<Row_Col> stack;  //观众
    boolean state;         //标记该房子 是否开局
    boolean turnWho;
    ArrayList<User> audiences;   // 观众席
    Queue<String> queueA; //B对A 说的话
    Queue<String> queueB; //A对B 说的话
    String isSuccess;
    public Home(User user) {
        // 创建房子
        userNum = 1;
        userA = user;
        userB = null;
        stackA = new Stack<>();
        stackB = new Stack<>();
        stack = new Stack<>();
        state = false;
        turnWho = Math.random() >= 0.5;  // ture ==>A 先下
        audiences = new ArrayList<>();
        queueA = new ArrayDeque<>();
        queueB = new ArrayDeque<>();
        isSuccess = "0";
    }

    private void Reset() {
        userA.setState("0");
        userB.setState("0");
        state = false;
    }

    private boolean isPlay() {
        if (userNum == 2) return userA.getState().equals("1") && userB.getState().equals("1");
        else return false;
    }

    public String inHome(User user) {
        /*
         *  这个业务得加锁
         * */
        if (userNum == 2) return "0";  // 房间满了 无法进入
        if (userNum == 1) userB = user;  // 房里有一个人 给userB赋值
        if (userNum == 0) userA = user;  // 房里没人  给userA赋值
        ++userNum;
        return "1";
    }

    public void outHome(User user) {
        // 优先删除观众
        boolean bool = audiences.removeIf(audience -> Objects.equals(audience.getId(), user.getId()));
        if (!bool) {
            if (Objects.equals(user.getId(), userA.getId())) userA = userB;  // A要走 userA = userB
            userB = null; //无论如何 userB 指向null
            --userNum;
        }
    }

    public String watchGame(User user) {
        if (audiences.size() < AUDIENCE_MAX) {
            audiences.add(user);
            return "1";
        }
        return "0";
    }

    public void prepare(User user, String state) {
        if (Objects.equals(user.getId(), userA.getId())) {
            userA.setState(state);
            return;
        }
        userB.setState(state);
    }

    public void playChess(User user, String row, String col, String isSuccess) {
        stack.push(new Row_Col(Integer.parseInt(row), Integer.parseInt(col)));
        if (Objects.equals(user.getId(), userA.getId())) {
            turnWho = false;
            stackA.push(new Row_Col(Integer.parseInt(row), Integer.parseInt(col)));
            if (Objects.equals(isSuccess, "1")) {
                this.isSuccess = userA.getName();
                Reset();
            }
        } else {
            turnWho = true;
            stackB.push(new Row_Col(Integer.parseInt(row), Integer.parseInt(col)));
            if (Objects.equals(isSuccess, "1")) {
                this.isSuccess = userB.getName();
                Reset();
            }
        }
    }

    public void backChess(User user) {
        if (Objects.equals(user.getId(), userA.getId())) {

        } else {

        }
    }

    public int getUserNum() {
        return userNum;
    }

    public String getInformation(String id) {
        if (userNum != 2) {
            return "等待用户进入";
        }
        if (!isPlay()) {
            return "等待用户准备";
        }
        state = true;
        if (Objects.equals(isSuccess, "0")) {
            if (turnWho) {
                if (Objects.equals(userA.getId(), id)) return "请您下棋";
                else return "等待对方下棋";
            } else {
                if (Objects.equals(userB.getId(), id)) return "请您下棋";
                else return "等待对方下棋";
            }
        } else {
            return isSuccess + "赢了";
        }
    }

    public String getUserA(String id) {
        if (Objects.equals(userA.getId(), id)) return userA.getName();
        return userB.getName();
    }

    public String getUserA() {
        return userA.getName();
    }

    public String getUserB(String id) {
        if (userNum != 2) return "暂无用户";
        if (Objects.equals(userA.getId(), id)) return userB.getName() + " " + userB.getState();
        return userA.getName() + " " + userA.getState();
    }

    public String getUserB() {
        if (userNum != 2) return "暂无用户";
        return userB.getName();
    }

    public String getGame(String id) {
        if (Objects.equals(userA.getId(), id)) {
            if (!stackB.isEmpty()) return stackB.peek().toString() + " " + isSuccess;
            else return "0";
        } else {
            if (!stackA.isEmpty()) return stackA.peek().toString() + " " + isSuccess;
            else return "0";
        }
    }

    public String getGame(int num) {
        if (num < stack.size()) return stack.peek().toString() + " " + isSuccess;
        else return "0";
    }

    public String getDialogue(String id) {
        if (Objects.equals(userA.getId(), id)) {
            if (!queueA.isEmpty()) return queueA.remove();
            return "0";
        }
        if (!queueB.isEmpty()) return queueB.remove();
        return "0";
    }

    public void setDialogue(User user, String date, String text) {
        if (Objects.equals(userA.getId(), user.getId())) {
            queueB.add(date + "!/@/#/@/!" + userA.getName() + "!/@/#/@/!" + text);
        } else {
            queueA.add(date + "!/@/#/@/!" + userB.getName() + "!/@/#/@/!" + text);
        }
    }

    /*
     * 记录房间信息
     *
     * */
    static class Row_Col {
        // 放旗子的位置
        Integer row;
        Integer col;

        Row_Col(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return row + " " + col;
        }
    }

}
