package dao;

import util.DBUtil;

import java.sql.*;

public class UserDAO
{
    public static boolean registerUser(String username, String password, String email) {
        if (userExists(username)){
            return false;
        }
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try(Connection conn = DBUtil.getConnection();PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static String getPasswordByUsername(String username) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");  // 返回数据库中的加密密码
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("查询密码时候出错\n" + e.getMessage());
        }
        return null;  // 用户不存在
    }

    public static boolean userExists(String username) {
        String sql = "SELECT username FROM users WHERE username = ?";
        try(Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            try(ResultSet rs = pstmt.executeQuery()){
                return rs.next();
            }

        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("查询用户名时候出错\n" + e.getMessage());
        }
        return false;
    }

    public static String getUserInfo(String username) {
        String sql = "SELECT username, email, created_at FROM users WHERE username = ?";
        try(Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String email = rs.getString("email");
                String registerTime = rs.getString("created_at");
                return "用户名: " + username + "\n邮箱: " + email + "\n注册时间: " + registerTime;
            }

        }catch(SQLException e){
            e.printStackTrace();
            return "查询用户时候出错\n" + e.getMessage();
        }
        return "用户不存在";
    }

    public static int getUserID(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try(Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                return rs.getInt("id");
            }
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("出错了，请重试\n" + e.getMessage());
        }
        return -1;
    }
}
