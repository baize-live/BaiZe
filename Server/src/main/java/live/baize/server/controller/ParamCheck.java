package live.baize.server.controller;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ParamCheck {

    @Pointcut("execution(* live.baize.server.controller.*Controller.*(..))")
    private void trimParam() {
        // 去掉前后空格
    }

    @Before("trimParam()")
    public void before(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        for (int i = 0; i < objects.length; ++i) {
            if (objects[i] != null && objects[i].getClass().equals(String.class)) {
                objects[i] = objects[i].toString().trim();
            }
        }
    }

}
