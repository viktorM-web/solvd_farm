package com.solvd.farm.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
@UtilityClass
public class ConnectionPool {

    private static final String URL_KEY = "db.url";
    private static final String USER_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final Integer DEFAULT_POOL_SIZE = 10;
    private static BlockingQueue<Connection> pool;
    private static List<Connection> sourceConnections;

    private static void initConnectionPool() {
        var poolSize = PropertiesUtil.get(POOL_SIZE_KEY);
        var size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        pool = new ArrayBlockingQueue<>(size);
        sourceConnections = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            var connection = open();
            var proxyConnection = (Connection)
                    Proxy.newProxyInstance(ConnectionPool.class.getClassLoader(), new Class[]{Connection.class},
                    (proxy, method, args) -> method.getName().equals("close")
                            ? pool.add((Connection) proxy)
                            : method.invoke(connection, args));
            pool.add(proxyConnection);
            sourceConnections.add(connection);

        }
    }

    public static Connection get() {
        if(pool==null){
            initConnectionPool();
        }

        try {
            return pool.take();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USER_KEY),
                    PropertiesUtil.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void closePool(){
        for (Connection connection: sourceConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }
}
