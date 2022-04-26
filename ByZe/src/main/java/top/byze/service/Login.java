package top.byze.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import top.byze.mapper.UserMapper;
import top.byze.utils.MyBatis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class Login {
    HttpServletRequest req;
    HttpServletResponse res;
    PrintWriter writer = null;

    public Login(HttpServletRequest req, HttpServletResponse res) {
        this.req = req;
        this.res = res;
        try {
            writer = this.res.getWriter();
        } catch (IOException e) {
            log.error("获得输出流异常");
        }
    }

    public void login() {
        MyBatis myBatis = new MyBatis();
        SqlSession sqlSession = myBatis.getSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        String email = this.req.getParameter("email");
        String password = this.req.getParameter("password");
        try {
            boolean flag = userMapper.findUser(email, password);
            if (flag) {
                writer.println("1");
                log.info("用户 " + email + " 登录成功");
            } else {
                writer.println("0");
                log.info("用户 " + email + " 登录失败");
            }
        } catch (Exception e) {
            writer.println("0");
            log.error("出异常了");
        }
        myBatis.closeSqlSession();
        writer.close();
    }
}
