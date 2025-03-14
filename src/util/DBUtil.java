package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static void initializeDatabase()
    {
        //Connection conn = null;
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DROP DATABASE IF EXISTS notebook_db;");
            stmt.executeUpdate("CREATE DATABASE notebook_db;");
            stmt.executeUpdate("USE notebook_db;");

            System.out.println("数据库 notebook_db 已重新创建！");

            // 执行 init.sql 脚本
            executeSQLScript("init.sql");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("数据库初始化失败: " + e.getMessage());
        }
    }

    private static void executeSQLScript(String filePath) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
                if (line.trim().endsWith(";")) { // 发现 SQL 语句结束符，执行 SQL
                    stmt.execute(sql.toString());
                    sql.setLength(0);
                }
            }
            System.out.println("SQL 脚本执行完成！");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            System.err.println("执行 SQL 脚本失败: " + e.getMessage());
        }
    }
}

