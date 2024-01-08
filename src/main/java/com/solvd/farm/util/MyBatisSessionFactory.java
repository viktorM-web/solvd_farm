package com.solvd.farm.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class MyBatisSessionFactory {

    private static SqlSessionFactory sessionFactory;

    static {
        init();
    }

    private static void init() {
        try(InputStream is = Resources.getResourceAsStream("mybatis-config.xml")) {
            sessionFactory = new SqlSessionFactoryBuilder()
                    .build(is);
        } catch (IOException exception) {
            log.error(exception.getMessage());
        }
    }

    public static SqlSession getSession() {
        return sessionFactory.openSession(true);
    }

}
