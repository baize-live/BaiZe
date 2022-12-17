package live.baize.server.service.user;

import live.baize.server.bean.business.User;
import org.apache.ibatis.session.SqlSession;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Service
@PropertySource("classpath:config.properties")
public class UserUtil {
    private final String Cookie_Name = "loginInfo";

    @Resource
    private HttpServletRequest request;
    @Resource
    private HttpServletResponse response;


    /**
     * 检查邮箱是否已经注册
     */
    public boolean checkEmail(String email) {
        return true;
    }

    /**
     * 添加用户
     */
    public boolean addUser(String username, String password, String email) {
        return true;
    }

    /**
     * 查找用户
     */
    public boolean findUser(String email, String password) {
        return true;
    }

    /**
     * 是否开通网盘
     */
    public boolean isOpenDisk() {
        return false;
    }

    /**
     * 是否开通游戏
     */
    public boolean isOpenGame() {
        return false;
    }

    /**
     * 开通网盘
     */
    public boolean openDisk() {
        return false;
//        // TODO: 事务管理
//        boolean flag = false;
//        try {
//            MyBatis myBatis = new MyBatis();
//            SqlSession sqlSession = myBatis.getSqlSession();
//            // 1. 修改user表中用户的isOpenPan字段
//            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
//            userMapper.updateUser(new User(email).setIsOpenPan("1"));
//            List<User> userList = userMapper.selectUser(new User(email));
//            Integer uid = null;
//            if (!userList.isEmpty()) {
//                uid = userList.get(0).getUid();
//            }
//            // 2. 在panData表中添加用户信息
//            PanDataMapper panDataMapper = sqlSession.getMapper(PanDataMapper.class);
//            panDataMapper.insertPanData(new PanData(uid));
//            // 3. 创建的文件目录
//            String path = ConfigUtil.getUserFilePath() + "User" + uid;
//            FileUtil.createDir(path);
//            // 关闭资源
//            myBatis.closeSqlSession();
//            flag = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("开通网盘异常");
//        }
//        return flag;
    }

    /**
     * 开通游戏
     */
    public boolean openGame() {
        return false;
//        boolean flag = false;
//        try {
//            MyBatis myBatis = new MyBatis();
//            SqlSession sqlSession = myBatis.getSqlSession();
//            // 获取 UserMapper 接口的代理对象
//            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
//            userMapper.updateUser(new User(email).setIsOpenYou("1"));
//            // 关闭资源
//            myBatis.closeSqlSession();
//            flag = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("开通游戏异常");
//        }
//        return flag;
    }

    // ========================================================= //

    /**
     * 添加cookies
     */
    public void setCookies(String email, String password) {
        email = Base64.getEncoder().encodeToString(email.getBytes());
        password = Base64.getEncoder().encodeToString(password.getBytes());
        Cookie cookie = new Cookie(Cookie_Name, email + "#" + password);
        cookie.setMaxAge(2592000);
        response.addCookie(cookie);
    }

    /**
     * 设置session
     */
    public void setSession(String email, String password) {
        request.getSession().setAttribute("user", new User(email).setPassword(password));
    }

    /**
     * 删除cookies
     */
    public void delCookies() {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    /**
     * 删除cookies
     */
    public void delSession() {
        request.getSession().setAttribute("user", null);
    }

    /**
     * 从Cookies中拿到User
     */
    public User getUserFromCookies() {
        User user = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (Cookie_Name.equals(cookie.getName())) {
                String[] params = cookie.getValue().split("#");
                user = new User(new String(Base64.getDecoder().decode(params[0]), StandardCharsets.UTF_8))
                        .setPassword(new String(Base64.getDecoder().decode(params[1]), StandardCharsets.UTF_8));
            }
        }
        return user;
    }

    /**
     * 从Session中拿到User
     */
    public User getUserFromSession() {
        return (User) request.getSession().getAttribute("user");
    }

}
