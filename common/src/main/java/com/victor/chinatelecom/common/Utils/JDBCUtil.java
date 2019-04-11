/**
 * @author Charles
 * @create 2019/4/2
 * @since 1.0.0
 */

package com.victor.chinatelecom.common.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
    private static final String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static final String MYSQL_URL =
            "jdbc:mysql://hadoop131:3306/ct?useUnicode=true&characterEncoding=UTF-8";
    private static final String MYSQL_USERNAME = "pcadmin";
    private static final String MYSQL_PASSWORD = "1024aA...";
    private static Connection conn = null;

    private JDBCUtil() {
    }

    public static Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName(MYSQL_DRIVER_CLASS);
                conn = DriverManager.getConnection(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return conn;

    }
}

