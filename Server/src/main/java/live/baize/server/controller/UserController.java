package live.baize.server.controller;

import live.baize.server.bean.business.User;
import live.baize.server.bean.exception.SystemException;
import live.baize.server.bean.response.Response;
import live.baize.server.bean.response.ResponseEnum;
import live.baize.server.service.user.UserUtil;
import live.baize.server.service.user.VerifyUtil;
import live.baize.server.service.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;

/**
 * @author CodeXS
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends HttpServlet {

    @Resource
    private MailUtil mailUtil;
    @Resource
    private UserUtil userUtil;
    @Resource
    private VerifyUtil verifyUtil;

    /**
     * 检查邮箱
     *
     * @param email 邮箱
     * @return Response 是否可用
     */
    @GetMapping("/checkEmail")
    public Response checkEmail(@RequestParam("email") String email) {
        // 判断是否存在此邮箱
        if (userUtil.checkEmail(email)) {
            return new Response(ResponseEnum.Has_Email);
        } else {
            return new Response(ResponseEnum.Not_Email);
        }
    }

    /**
     * 发送验证码
     *
     * @param email 邮箱
     * @return Response 是否可用
     */
    @GetMapping("/sendVerifyCode")
    public Response sendVerifyCode(@RequestParam("email") String email) {
        // 获取注册码
        String verifyCode = verifyUtil.generateVerifyCode();
        // 保存验证码
        verifyUtil.saveVerifyCode(email, verifyCode);
        // 发送邮件
        try {
            mailUtil.sendVerifyCode(email, verifyCode);
            return new Response(ResponseEnum.VerifyCode_Send_Success);
        } catch (Exception e) {
            throw new SystemException(ResponseEnum.VerifyCode_Send_Failure, e.getCause());
        }
    }

    /**
     * 注册
     *
     * @param email      邮箱
     * @param username   用户名
     * @param password   密码
     * @param verifyCode 验证码
     * @return Response 注册成功
     */
    @GetMapping("/register")
    public Response register(@RequestParam("email") String email,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("verifyCode") String verifyCode) {
        if (verifyUtil.checkVerifyCode(email, verifyCode)) {
            if (userUtil.addUser(username, password, email)) {
                return new Response(ResponseEnum.Register_Success);
            }
        }
        return new Response(ResponseEnum.Register_Failure);
    }

    /**
     * 登录
     */
    @GetMapping("/login")
    public Response login(@RequestParam("email") String email, @RequestParam("password") String password) {
        if (userUtil.findUser(email, password)) {
            userUtil.setCookies(email, password);
            userUtil.setSession(email, password);
            return new Response(ResponseEnum.Login_Success);
        } else {
            return new Response(ResponseEnum.Login_Failure);
        }
    }

    /**
     * 是否登录
     */
    @GetMapping("/isLogin")
    public Response isLogin() {
        if (userUtil.getUserFromSession() != null) {
            return new Response(ResponseEnum.Has_Login);
        }
        User user = userUtil.getUserFromCookies();
        if (user != null) {
            if (userUtil.findUser(user.getEmail(), user.getPassword())) {
                userUtil.setSession(user.getEmail(), user.getPassword());
                return new Response(ResponseEnum.Has_Login);
            }
        }
        return new Response(ResponseEnum.Not_Login);
    }

    /**
     * 登出
     */
    @GetMapping("/logout")
    public Response logout() {
        userUtil.delCookies();
        userUtil.delSession();
        return new Response(ResponseEnum.Logout_Success);
    }

    /**
     * 开通网盘
     */
    @GetMapping("/openDisk")
    public Response openDisk() {
        // 查询数据库 判断是否开启游戏
        if (userUtil.openDisk()) {
            return new Response(ResponseEnum.OpenDisk_Success);
        } else {
            return new Response(ResponseEnum.OpenDisk_Failure);
        }
    }

    /**
     * 返回是否开通网盘
     */
    @GetMapping("/isOpenDisk")
    public Response isOpenDisk() {
        // 查询数据库 判断是否开启白泽网盘
        if (userUtil.isOpenDisk()) {
            return new Response(ResponseEnum.Has_OpenDisk);
        } else {
            return new Response(ResponseEnum.Not_OpenDisk);
        }
    }

    /**
     * 开通游戏
     */
    @GetMapping("/openGame")
    public Response openGame() {
        // 查询数据库 判断是否开启游戏
        if (userUtil.openGame()) {
            return new Response(ResponseEnum.OpenGame_Success);
        } else {
            return new Response(ResponseEnum.OpenGame_Failure);
        }
    }

    /**
     * 返回是否开通游戏
     */
    @GetMapping("/isOpenGame")
    public Response isOpenGame() {
        // 查询数据库 判断是否开启游戏
        if (userUtil.isOpenGame()) {
            return new Response(ResponseEnum.Has_OpenGame);
        } else {
            return new Response(ResponseEnum.Not_OpenGame);
        }
    }

}
