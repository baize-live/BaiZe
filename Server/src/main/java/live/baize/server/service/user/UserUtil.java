package live.baize.server.service.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import live.baize.server.bean.business.User;
import live.baize.server.mapper.user.UserMapper;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@PropertySource("classpath:config.properties")
public class UserUtil {
    private final String Cookie_Name = "loginInfo";

    @Resource
    private HttpServletRequest request;
    @Resource
    private HttpServletResponse response;
    @Resource
    private UserMapper userMapper;

    /**
     * 检查邮箱是否已经注册
     */
    public boolean checkEmail(String email) {
        return userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("email", email)
                        .select("UID")
        ) != null;
    }

    /**
     * 添加用户
     */
    public boolean addUser(String username, String password, String email) {
        return userMapper.insert(
                new User(email).setUsername(username).setPassword(password)
        ) == 1;
    }

    /**
     * 删除用户
     */
    public boolean delUser(String email) {
        // TODO: 删除用户
//          需要考虑
//          1. 先删除所有业务表中的数据
//          2. 再删除用户表
        return userMapper.delete(
                new QueryWrapper<User>()
                        .eq("email", email)
        ) == 1;
    }

    /**
     * 查找用户
     */
    public boolean findUser(String email, String password) {
        return userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("email", email)
                        .eq("password", password)
                        .select("UID")
        ) != null;
    }

    /**
     * 是否开通网盘
     */
    public boolean isOpenDisk() {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return false;
        }
        return "1".equals(userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("email", user.getEmail())
                        .eq("password", user.getPassword())
                        .select("isOpenDisk")
        ).getIsOpenDisk());
    }

    /**
     * 是否开通游戏
     */
    public boolean isOpenGame() {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return false;
        }
        return "1".equals(userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq("email", user.getEmail())
                        .eq("password", user.getPassword())
                        .select("isOpenGame")
        ).getIsOpenGame());
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
        if (cookies == null) {
            return;
        }
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
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        User user = null;
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
