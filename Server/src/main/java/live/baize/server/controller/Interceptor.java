package live.baize.server.controller;

import live.baize.server.bean.exception.BusinessException;
import live.baize.server.bean.response.ResponseEnum;
import live.baize.server.service.user.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Interceptor implements HandlerInterceptor {
    // 允许不登录的URI
    static List<String> AllowNotLoginURI = new ArrayList<>();

    static {
        AllowNotLoginURI.add("/");
        AllowNotLoginURI.add("/user/checkEmail");
        AllowNotLoginURI.add("/user/sendVerifyCode");
        AllowNotLoginURI.add("/user/register");
        AllowNotLoginURI.add("/user/login");
        AllowNotLoginURI.add("/user/isLogin");
        AllowNotLoginURI.add("/user/logout");
    }

    @Resource
    SessionUtil sessionUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws BusinessException {
        log.info("request: " + request.getRequestURI());

        // 首先检查是否需要登录
        if (AllowNotLoginURI.contains(request.getRequestURI())) {
            return true;
        }

        // 已经登录则放行
        if (sessionUtil.getUserFromSession() != null) {
            return true;
        }

        // 否则返回未登录
        throw new BusinessException(ResponseEnum.Not_Login);
    }
}
