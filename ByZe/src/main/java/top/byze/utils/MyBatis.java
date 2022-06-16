package top.byze.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author CodeXS
 */
@Slf4j
public class MyBatis {
    final SqlSession sqlSession;

    public MyBatis() {
        // 加载mybatis的核心配置文件, 获取SqlSessionFactory
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        } catch (IOException e) {
            log.error("实例化MyBatis时 IO异常");
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 获得SqlSession对象 用它执行sql
        sqlSession = sqlSessionFactory.openSession(true);
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void closeSqlSession() {
        sqlSession.close();
    }
}
