package live.baize.server.controller;

import live.baize.server.bean.response.Response;
import live.baize.server.bean.response.ResponseEnum;
import live.baize.server.service.utils.MailUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalTime;

@Configuration
@RestController
@RequestMapping("/")
public class Config {

    Integer days = 0;
    @Resource
    MailUtil mailUtil;

    @GetMapping
    public Response helloWorld() {
        return new Response(ResponseEnum.Hello_World);
    }

    /**
     * 一天一次保活 每天晚上10点发一条
     */
    @Scheduled(initialDelay = 0, fixedRate = 3600000)
    public void keepAlive() {
        if (LocalTime.now().getHour() == 22) {
            mailUtil.sendKeepAliveMail(++days);
        }
    }

}


