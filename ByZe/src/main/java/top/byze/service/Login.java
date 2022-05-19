package top.byze.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import top.byze.bean.User;
import top.byze.mapper.PanDataMapper;
import top.byze.mapper.UserMapper;
import top.byze.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class Login {
    HttpServletRequest req;
    HttpServletResponse res;
    PrintWriter writer = null;

    private static class Res {
        final static String TRUE = "1";
        final static String FALSE = "0";
    }

    public Login(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
        try {
            writer = this.res.getWriter();
        } catch (IOException e) {
            log.error("获得输出流异常");
        }
    }

    // =================数据库交互 使用静态
    // 是否开通网盘
    private static boolean isOpenPan(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            flag = userMapper.isOpenPan(email);
            // 关闭资源
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("检查是否开通网盘异常");
        }
        return flag;
    }

    // 是否开通游戏
    private static boolean isOpenYou(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            flag = userMapper.isOpenYou(email);
            // 关闭资源
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("检查是否开通游戏异常");
        }
        return flag;
    }

    // 开通网盘
    private static boolean openPan(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 1. 修改user表中用户的isOpenPan字段
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.openPan(email);
            int Uid = userMapper.getUid(email);
            // 2. 在panData表中添加用户信息
            PanDataMapper panDataMapper = sqlSession.getMapper(PanDataMapper.class);
            panDataMapper.initData(Uid);
            // 3. 创建的文件目录
            String path = ConfigUtil.getUserFilePath() + "User" + Uid;
            FileUtil.createDir(path);
            // 关闭资源
            myBatis.closeSqlSession();
            flag = true;
        } catch (Exception e) {
            log.error("开通网盘异常");
        }
        return flag;
    }

    // 开通游戏
    private static boolean openYou(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.openYou(email);
            // 关闭资源
            myBatis.closeSqlSession();
            flag = true;
        } catch (Exception e) {
            log.error("开通游戏异常");
        }
        return flag;
    }

    // 查找用户
    private static boolean findUser(String email, String password) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            flag = userMapper.findUser(email, password);
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("查找用户异常");
        }
        return flag;
    }

    // 添加cookies
    private static void addCookies(String email, String password, HttpServletResponse res) {
        User user = new User(password, email);
        CookieUtil.set(user, res);
    }

    // 设置session
    private static void setSession(String email, String password, HttpServletRequest req) {
        User user = new User(password, email);
        SessionUtil.set(user, req);
    }

    // =================提供给Filter的接口
    public static boolean isLogin(HttpServletRequest req) {
        boolean flag = false;

        if (SessionUtil.getUser(req) == null) {
            Map<String, String> cookies = CookieUtil.get(req);
            if (cookies != null) {
                String email = cookies.get("email");
                String password = cookies.get("password");
                if (Login.findUser(email, password)) {
                    SessionUtil.set(new User(password, email), req);
                    flag = true;
                }
            }
        } else {
            flag = true;
        }

        return flag;
    }

    // 返回是否开通网盘
    public static boolean isOpenPan(HttpServletRequest req) {
        User user = SessionUtil.getUser(req);
        return isOpenPan(user.getEmail());
    }

    // =================提供给Servlet的接口
    // 登录
    public void login() {
        String email = this.req.getParameter("email");
        String password = this.req.getParameter("password");
        boolean flag = findUser(email, password);
        try {
            if (flag) {
                addCookies(email, password, this.res);
                setSession(email, password, this.req);
                writer.println(Res.TRUE);
                log.info("用户 " + email + " 登录成功");
            } else {
                writer.println(Res.FALSE);
                log.info("用户 " + email + " 登录失败");
            }
        } catch (Exception e) {
            writer.println(Res.FALSE);
            log.error("登录异常");
        }
        writer.close();
    }

    // 返回是否开通网盘
    public void isOpenPan() {
        // 查询数据库 判断是否开启白泽网盘
        User user = SessionUtil.getUser(req);
        boolean flag = isOpenPan(user.getEmail());
        if (flag) {
            writer.println(Res.TRUE);
            log.info(user.getEmail() + "网盘已经开通");
        } else {
            writer.println(Res.FALSE);
            log.info(user.getEmail() + "网盘暂未开通");
        }
        writer.close();
    }

    // 返回是否开通游戏
    public void isOpenYou() {
        // 查询数据库 判断是否开启白泽库
        User user = SessionUtil.getUser(req);
        boolean flag = isOpenYou(user.getEmail());
        if (flag) {
            writer.println(Res.TRUE);
            log.info(user.getEmail() + "游戏已经开通");
        } else {
            writer.println(Res.FALSE);
            log.info(user.getEmail() + "游戏暂未开通");
        }
        writer.close();
    }

    // 开通网盘
    public void openPan() {
        User user = SessionUtil.getUser(req);
        if (openPan(user.getEmail())) {
            writer.println(Res.TRUE);
            log.info(user.getEmail() + "网盘开通成功");
        } else {
            writer.println(Res.FALSE);
            log.info(user.getEmail() + "网盘开通失败");
        }
        writer.close();
    }

    // 开通游戏
    public void openYou() {
        User user = SessionUtil.getUser(req);
        if (openYou(user.getEmail())) {
            writer.println(Res.TRUE);
            log.info(user.getEmail() + "游戏开通成功");
        } else {
            writer.println(Res.FALSE);
            log.info(user.getEmail() + "游戏开通失败");
        }
        writer.close();
    }

    // 登出
    public void logout() {
        // 清除cookies
        CookieUtil.delete(this.req, this.res);
        writer.println(Res.TRUE);
        writer.close();
    }

    // 是否登录
    public void isLogin() {
        if (isLogin(req)) {
            writer.println(Res.TRUE);
        } else {
            writer.println(Res.FALSE);
        }
        writer.close();
    }
}
