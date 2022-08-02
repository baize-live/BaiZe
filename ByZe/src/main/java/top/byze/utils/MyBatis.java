package top.byze.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author CodeXS
 */
@Slf4j
public class MyBatis {
    final SqlSession sqlSession;
    InputStream inputStream = null;

    public MyBatis() {
        try {
            // 加载mybatis的核心配置文件, 获取SqlSessionFactory
            inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        } catch (IOException e) {
            log.error("实例化MyBatis时 IO异常" + e.getMessage());
        }
        // 获得SqlSession对象 用它执行sql
        sqlSession = new SqlSessionFactoryBuilder().build(inputStream).openSession(true);
    }

    public SqlSession getSqlSession() {
        return sqlSession;
    }

    public void closeSqlSession() {
        if (sqlSession != null) {
            sqlSession.close();
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("MyBatis 关闭inputStream 异常" + e.getMessage());
            }
        }
    }
}
