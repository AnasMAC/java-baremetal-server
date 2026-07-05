package com.pfe.prep.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import org.postgresql.core.SqlCommand;
import org.postgresql.replication.fluent.logical.StartLogicalReplicationCallback;

/**
 * CustomConnectionPool
 */
public class CustomConnectionPool {

    private static final String DB_URL =
        "jdbc:postgresql://localhost:5432/barmetal1";
    private static final String USER = "mac";
    private static final String PASS = "Anasgamer@2";
    private static int ConnectionNumber = 10;
    private ArrayBlockingQueue<Connection> queue = new ArrayBlockingQueue<>(
        ConnectionNumber
    );

    public CustomConnectionPool() {
        try {
            for (int i = 0; i < ConnectionNumber; i++) {
                Connection c = DriverManager.getConnection(DB_URL, USER, PASS);
                queue.add(c);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws InterruptedException {
        return queue.take();
    }

    public void releaseConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                queue.offer(connection);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
