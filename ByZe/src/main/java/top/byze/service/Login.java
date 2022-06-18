package top.byze.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import top.byze.bean.PanData;
import top.byze.bean.User;
import top.byze.mapper.PanDataMapper;
import top.byze.mapper.UserMapper;
import top.byze.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author CodeXS
 */
@Slf4j
public class Login {
    final HttpServletRequest req;
    final HttpServletResponse res;

    public Login(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
    }

    // ========================================与数据库交互=============================================== //

    /**
     * 是否开通网盘
     */
    private static boolean isOpenPan(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            flag = "1".equals(userMapper.selectUser(new User(email)).getIsOpenPan());
            // 关闭资源
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("检查是否开通网盘异常");
        }
        return flag;
    }

    /**
     * 是否开通游戏
     */
    private static boolean isOpenYou(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            flag = "1".equals(userMapper.selectUser(new User(email)).getIsOpenYou());
            // 关闭资源
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("检查是否开通游戏异常");
        }
        return flag;
    }

    /**
     * 开通网盘
     */
    private static boolean openPan(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 1. 修改user表中用户的isOpenPan字段
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.updateUser(new User(email).setIsOpenPan("1"));
            int userId = userMapper.selectUser(new User(email)).getUid();
            // 2. 在panData表中添加用户信息
            PanDataMapper panDataMapper = sqlSession.getMapper(PanDataMapper.class);
            panDataMapper.insertPanData(new PanData(userId));
            // 3. 创建的文件目录
            String path = ConfigUtil.getUserFilePath() + "User" + userId;
            FileUtil.createDir(path);
            // 关闭资源
            myBatis.closeSqlSession();
            flag = true;
        } catch (Exception e) {
            log.error("开通网盘异常");
        }
        return flag;
    }

    /**
     * 开通游戏
     */
    private static boolean openYou(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.updateUser(new User(email).setIsOpenYou("1"));
            // 关闭资源
            myBatis.closeSqlSession();
            flag = true;
        } catch (Exception e) {
            log.error("开通游戏异常");
        }
        return flag;
    }

    /**
     * 查找用户
     */
    private static boolean findUser(String email, String password) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.selectUser(new User(email));
            if (user != null && user.getPassword().equals(password)) {
                flag = true;
            }
            myBatis.closeSqlSession();
        } catch (Exception e) {
            log.error("查找用户异常");
        }
        return flag;
    }

    /**
     * 添加cookies
     */
    private static void addCookies(String email, String password, HttpServletResponse res) {
        User user = new User(email).setPassword(password);
        CookieUtil.set(user, res);
    }

    /**
     * 设置session
     */
    private static void setSession(String email, String password, HttpServletRequest req) {
        User user = new User(email).setPassword(password);
        SessionUtil.set(user, req);
    }

    // ========================================Filter接口================================================== //

    /**
     * 返回是否登录
     *
     * @param req 请求
     * @return true:已登录，false:未登录
     */
    public static boolean isLogin(HttpServletRequest req) {
        boolean flag = false;

        if (SessionUtil.getUser(req) == null) {
            Map<String, String> cookies = CookieUtil.get(req);
            if (cookies != null) {
                String email = cookies.get("email");
                String password = cookies.get("password");
                if (Login.findUser(email, password)) {
                    SessionUtil.set(new User(email).setPassword(password), req);
                    flag = true;
                }
            }
        } else {
            flag = true;
        }

        return flag;
    }

    /**
     * 返回是否开通网盘
     *
     * @param req HttpServletRequest
     * @return boolean
     */
    public static boolean isOpenPan(HttpServletRequest req) {
        User user = SessionUtil.getUser(req);
        return isOpenPan(user.getEmail());
    }

    // ========================================Servlet接口================================================= //

    /**
     * 登录
     */
    public void login() {
        String email = this.req.getParameter("email");
        String password = this.req.getParameter("password");
        boolean flag = findUser(email, password);
        try {
            if (flag) {
                addCookies(email, password, this.res);
                setSession(email, password, this.req);
                this.res.getWriter().println(Res.TRUE);
                log.info(email + " 登录成功");
            } else {
                this.res.getWriter().println(Res.FALSE);
                log.info(email + " 登录失败");
            }
        } catch (Exception e) {
            log.error("登录异常");
        }
    }

    /**
     * 返回是否开通网盘
     */
    public void isOpenPan() {
        // 查询数据库 判断是否开启白泽网盘
        User user = SessionUtil.getUser(req);
        boolean flag = isOpenPan(user.getEmail());
        try {
            if (flag) {
                this.res.getWriter().println(Res.TRUE);
                log.info(user.getEmail() + " 网盘已经开通");
            } else {
                this.res.getWriter().println(Res.FALSE);
                log.info(user.getEmail() + " 网盘暂未开通");
            }
        } catch (Exception e) {
            log.error("返回是否开通网盘异常");
        }
    }

    /**
     * 返回是否开通游戏
     */
    public void isOpenYou() {
        // 查询数据库 判断是否开启白泽库
        User user = SessionUtil.getUser(req);
        boolean flag = isOpenYou(user.getEmail());
        try {
            if (flag) {
                this.res.getWriter().println(Res.TRUE);
                log.info(user.getEmail() + " 游戏已经开通");
            } else {
                this.res.getWriter().println(Res.FALSE);
                log.info(user.getEmail() + " 游戏暂未开通");
            }
        } catch (Exception e) {
            log.error("返回是否开通游戏异常");
        }
    }

    /**
     * 开通网盘
     */
    public void openPan() {
        User user = SessionUtil.getUser(req);
        try {
            if (openPan(user.getEmail())) {
                this.res.getWriter().println(Res.TRUE);
                log.info(user.getEmail() + " 网盘开通成功");
            } else {
                this.res.getWriter().println(Res.FALSE);
                log.info(user.getEmail() + " 网盘开通失败");
            }
        } catch (Exception e) {
            log.error("开通网盘异常");
        }
    }

    /**
     * 开通游戏
     */
    public void openYou() {
        User user = SessionUtil.getUser(req);
        try {
            if (openYou(user.getEmail())) {
                this.res.getWriter().println(Res.TRUE);
                log.info(user.getEmail() + " 游戏开通成功");
            } else {
                this.res.getWriter().println(Res.FALSE);
                log.info(user.getEmail() + " 游戏开通失败");
            }
        } catch (Exception e) {
            log.error("开通游戏异常");
        }
    }

    /**
     * 登出
     */
    public void logout() {
        // 清除cookies
        CookieUtil.delete(this.req, this.res);
        try {
            this.res.getWriter().println(Res.TRUE);
        } catch (Exception e) {
            log.error("退出登录异常");
        }
    }

    /**
     * 是否登录
     */
    public void isLogin() {
        try {
            if (isLogin(req)) {
                this.res.getWriter().println(Res.TRUE);
            } else {
                this.res.getWriter().println(Res.FALSE);
            }
        } catch (Exception e) {
            log.error("退出登录异常");
        }
    }

    /**
     * 返回值
     */
    private static class Res {
        final static String TRUE = "1";
        final static String FALSE = "0";
    }

}
