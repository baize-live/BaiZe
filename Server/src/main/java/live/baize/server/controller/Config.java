package live.baize.server.controller;

import live.baize.server.bean.response.Response;
import live.baize.server.bean.response.ResponseEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Config {

    @GetMapping
    public Response helloWorld() {
        return new Response(ResponseEnum.Hello_World);
    }

}


