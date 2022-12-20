package live.baize.server.controller;

import live.baize.server.bean.business.User;
import live.baize.server.bean.exception.SystemException;
import live.baize.server.bean.response.Response;
import live.baize.server.bean.response.ResponseEnum;
import live.baize.server.service.user.SessionUtil;
import live.baize.server.service.user.UserUtil;
import live.baize.server.service.utils.RandomUtil;
import live.baize.server.service.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.validation.constraints.Email;

@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController extends HttpServlet {
    @Resource
    private MailUtil mailUtil;
    @Resource
    private UserUtil userUtil;
    @Resource
    private RandomUtil randomUtil;
    @Resource
    private SessionUtil sessionUtil;

    /**
     * 检查邮箱
     *
     * @param email 邮箱
     * @return Response 是否可用
     */
    @GetMapping("/checkEmail")
    public Response checkEmail(@Email @RequestParam("email") String email) {
        // 判断是否存在此邮箱
        if (userUtil.checkEmailIsRegister(email)) {
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
    public Response sendVerifyCode(@Email @RequestParam("email") String email) {
        // 获取验证码
        String verifyCode = randomUtil.generateVerifyCode();
        // 保存验证码
        if (userUtil.saveVerifyCode(email, verifyCode)) {
            // 发送邮件
            try {
                mailUtil.sendVerifyCode(email, verifyCode);
                return new Response(ResponseEnum.VerifyCode_Send_Success);
            } catch (Exception e) {
                throw new SystemException(ResponseEnum.VerifyCode_Send_Failure, e.getCause());
            }
        }
        return new Response(ResponseEnum.VerifyCode_Send_Failure);
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
    public Response register(
            @Email @RequestParam("email") String email,
            @Length(min = 1, max = 20, message = "username 长度必须在{min}和{max}之间") @RequestParam("username") String username,
            @Length(min = 10, max = 30, message = "password 长度必须在{min}和{max}之间") @RequestParam("password") String password,
            @Length(min = 6, max = 6, message = "verifyCode 长度必须为6") @RequestParam("verifyCode") String verifyCode) {
        if (userUtil.checkVerifyCode(email, verifyCode)) {
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
    public Response login(
            @Email @RequestParam("email") String email,
            @Length(min = 10, max = 30, message = "password 长度必须在{min}和{max}之间") @RequestParam("password") String password) {
        if (userUtil.findUser(email, password)) {
            sessionUtil.setSession(email);
            sessionUtil.setCookies(email, password);
            return new Response(ResponseEnum.Login_Success);
        }
        return new Response(ResponseEnum.Login_Failure);
    }

    /**
     * 是否登录
     */
    @GetMapping("/isLogin")
    public Response isLogin() {
        if (sessionUtil.getUserFromSession() != null) {
            return new Response(ResponseEnum.Has_Login);
        }
        User user = sessionUtil.getUserFromCookies();
        if (user != null) {
            if (userUtil.findUser(user.getEmail(), user.getPassword())) {
                sessionUtil.setSession(user.getEmail());
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
        sessionUtil.delCookies();
        sessionUtil.delSession();
        return new Response(ResponseEnum.Logout_Success);
    }

    /**
     * 开通网盘
     */
    @GetMapping("/openDisk")
    public Response openDisk() {
        if (userUtil.openDisk(sessionUtil.getUserFromSession().getEmail())) {
            return new Response(ResponseEnum.OpenDisk_Success);
        }
        return new Response(ResponseEnum.OpenDisk_Failure);
    }

    /**
     * 返回是否开通网盘
     */
    @GetMapping("/isOpenDisk")
    public Response isOpenDisk() {
        if (userUtil.isOpenDisk(sessionUtil.getUserFromSession().getEmail())) {
            return new Response(ResponseEnum.Has_OpenDisk);
        }
        return new Response(ResponseEnum.Not_OpenDisk);
    }

    /**
     * 开通游戏
     */
    @GetMapping("/openGame")
    public Response openGame() {
        if (userUtil.openGame(sessionUtil.getUserFromSession().getEmail())) {
            return new Response(ResponseEnum.OpenGame_Success);
        }
        return new Response(ResponseEnum.OpenGame_Failure);
    }

    /**
     * 返回是否开通游戏
     */
    @GetMapping("/isOpenGame")
    public Response isOpenGame() {
        if (userUtil.isOpenGame(sessionUtil.getUserFromSession().getEmail())) {
            return new Response(ResponseEnum.Has_OpenGame);
        }
        return new Response(ResponseEnum.Not_OpenGame);
    }

}
