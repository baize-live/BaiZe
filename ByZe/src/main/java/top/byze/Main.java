package top.byze;

import org.apache.ibatis.session.SqlSession;
import top.byze.bean.User;
import top.byze.mapper.UserMapper;
import top.byze.utils.MyBatis;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // 获得SqlSession对象 用它执行sql
        MyBatis myBatis = new MyBatis();
        SqlSession sqlSession = myBatis.getSqlSession();
        // 获取UserMapper接口的代理对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.selectAll();
        for (User user : users) {
            System.out.println(user);
        }
//        userMapper.delAll();
        // 提交事务
        sqlSession.commit();
        // 释放资源
        myBatis.closeSqlSession();
    }
}
