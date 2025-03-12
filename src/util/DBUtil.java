package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // MySQL 数据库连接信息
    private static final String URL = "jdbc:mysql://localhost:3306/notebook_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";  // 你的 MySQL 用户名
    private static final String PASSWORD = "Fumini092821";  // 你的 MySQL 密码

    static {
        try {
            // 加载 MySQL 驱动（可省略）
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 获取数据库连接
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 关闭连接
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

