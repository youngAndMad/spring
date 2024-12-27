package kz.danekerscode.jdbc;

import lombok.Cleanup;
import org.springframework.util.StringUtils;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class JdbcConnectionPool {
    private final static String DB_URL = "db.url";
    private final static String DB_USERNAME = "db.username";
    private final static String DB_PASSWORD = "db.password";
    private final static String DB_POOL_SIZE = "db.pool.size";
    private static final String CLOSE = "close";
    private static final int DEFAULT_POOL_SIZE = 10;

    private static final BlockingQueue<Connection> connectionPool;
    private static final List<Connection> sourceConnections;

    static {
        loadDriver();

        var poolSizeFromProperties = PropertyUtil.getJdbcProperty(DB_POOL_SIZE);

        var poolSize = StringUtils.hasText(poolSizeFromProperties) ? Integer.parseInt(poolSizeFromProperties) : DEFAULT_POOL_SIZE;

        connectionPool = new ArrayBlockingQueue<>(poolSize);
        sourceConnections = new ArrayList<>(poolSize);

        for (int i = 0; i < poolSize; i++) {
            var connection = createConnection();
            var proxyConnection = (Connection) Proxy.newProxyInstance(JdbcConnectionPool.class.getClassLoader(), new Class[]{Connection.class}, (proxy, method, args) -> {
                if (method.getName().equals(CLOSE)) {
                    return connectionPool.add(connection);
                }
                return method.invoke(connection, args);
            });
            sourceConnections.add(connection);
            connectionPool.add(proxyConnection);
        }
    }

    public static Connection get() {
        try {
            return connectionPool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection createConnection() {
        try {
            return DriverManager.getConnection(
                    PropertyUtil.getJdbcProperty(DB_URL),
                    PropertyUtil.getJdbcProperty(DB_USERNAME),
                    PropertyUtil.getJdbcProperty(DB_PASSWORD)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void shutdown() {
        sourceConnections.forEach(connection -> {
            try {
                connection.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
