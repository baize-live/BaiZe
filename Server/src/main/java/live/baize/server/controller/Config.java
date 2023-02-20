package live.baize.server.controller;

import live.baize.server.bean.response.Response;
import live.baize.server.bean.response.ResponseEnum;
import live.baize.server.service.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalTime;

@Slf4j
@Configuration
@RestController
@RequestMapping("/")
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600) // 分布式session实现
public class Config implements HandlerInterceptor, WebMvcConfigurer {

    Integer days = 0;
    @Resource
    private MailUtil mailUtil;
    @Resource
    private Interceptor interceptor;

    @GetMapping
    public Response helloWorld() {
        return new Response(ResponseEnum.Hello_World);
    }

    /**
     * 七天一次保活 晚上10点发一条
     */
    @Scheduled(initialDelay = 0, fixedRate = 3600000)
    public void keepAlive() {
        if (days++ % 7 == 0 && LocalTime.now().getHour() == 22) {
            mailUtil.sendKeepAliveMail(days);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 总入口
        log.info(request.getRequestURI());
        return true;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this)
                .addPathPatterns("/**");

        registry.addInterceptor(interceptor)
                .addPathPatterns("/user/hasLogin/*")
                .addPathPatterns("/baize*/*");
    }

}


