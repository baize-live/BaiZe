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
import java.util.List;
import java.util.Map;

/**
 * @author CodeXS
 */
@Slf4j
public class Login {
    // ========================================与数据库交互=============================================== //

    /**
     * 是否开通网盘
     */
    public static boolean isOpenPan(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            // 拿到查询结果
            List<User> userList = userMapper.selectUser(new User(email));
            if (!userList.isEmpty()) {
                flag = "1".equals(userList.get(0).getIsOpenPan());
            }
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
    public static boolean isOpenYou(String email) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 获取 UserMapper 接口的代理对象
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            // 拿到查询结果
            List<User> userList = userMapper.selectUser(new User(email));
            if (!userList.isEmpty()) {
                flag = "1".equals(userList.get(0).getIsOpenYou());
            }
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
    public static boolean openPan(String email) {
        // TODO: 事务管理
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            // 1. 修改user表中用户的isOpenPan字段
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.updateUser(new User(email).setIsOpenPan("1"));
            List<User> userList = userMapper.selectUser(new User(email));
            Integer uid = null;
            if (!userList.isEmpty()) {
                uid = userList.get(0).getUid();
            }
            // 2. 在panData表中添加用户信息
            PanDataMapper panDataMapper = sqlSession.getMapper(PanDataMapper.class);
            panDataMapper.insertPanData(new PanData(uid));
            // 3. 创建的文件目录
            String path = ConfigUtil.getUserFilePath() + "User" + uid;
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
    public static boolean openYou(String email) {
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
    public static boolean findUser(String email, String password) {
        boolean flag = false;
        try {
            MyBatis myBatis = new MyBatis();
            SqlSession sqlSession = myBatis.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.selectUser(new User(email));
            if (!userList.isEmpty() && userList.get(0).getPassword().equals(password)) {
                flag = true;
            }
            myBatis.closeSqlSession();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查找用户异常");
        }
        return flag;
    }

    /**
     * 添加cookies
     */
    public static void addCookies(String email, String password, HttpServletResponse res) {
        User user = new User(email).setPassword(password);
        CookieUtil.set(user, res);
    }

    /**
     * 设置session
     */
    public static void setSession(String email, String password, HttpServletRequest req) {
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

}
