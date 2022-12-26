package live.baize.server.controller;

import live.baize.server.bean.business.User;
import live.baize.server.bean.exception.BusinessException;
import live.baize.server.bean.response.ResponseEnum;
import live.baize.server.service.user.SessionUtil;
import live.baize.server.service.user.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class Interceptor implements HandlerInterceptor {
    @Resource
    private UserUtil userUtil;

    @Resource
    private SessionUtil sessionUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws BusinessException {
        // 已经登录则放行
        if (sessionUtil.getUserFromSession() != null) {
            return true;
        }

        // 未登录尝试登录
        User user = sessionUtil.getUserFromCookies();
        if (user != null) {
            if (userUtil.findUser(user.getEmail(), user.getPassword())) {
                sessionUtil.setSession(user.getEmail());
                return true;
            }
        }

        // 否则返回未登录
        throw new BusinessException(ResponseEnum.Not_Login);
    }
}
