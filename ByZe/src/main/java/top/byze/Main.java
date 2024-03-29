package top.byze;

import org.apache.ibatis.session.SqlSession;
import top.byze.bean.User;
import top.byze.mapper.UserMapper;
import top.byze.utils.MyBatis;

import java.util.List;

/**
 * @author CodeXS
 */
public class Main {
    public static void main(String[] args) {
        // 获得SqlSession对象 用它执行sql
        MyBatis myBatis = new MyBatis();
        SqlSession sqlSession = myBatis.getSqlSession();
        // 获取UserMapper接口的代理对象
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.selectUser(new User("1921676794@qq.com"));
        for (User user : users) {
            System.out.println(user);
        }
        // 提交事务
        sqlSession.commit();
        // 释放资源
        myBatis.closeSqlSession();
    }
}
