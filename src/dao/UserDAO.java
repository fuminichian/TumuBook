package dao;

import util.DBUtil;
import model.User;
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
                return rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("查询密码时候出错\n" + e.getMessage());
        }
        return null;
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

    public static User getUserInfo(String username) {
        String sql = "SELECT username, email, created_at FROM users WHERE username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("created_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("查询用户信息时出错：" + e.getMessage());
        }
        return null;  // 查询不到返回 null
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

    public static boolean deleteUser(int userId) {
        String sql1 = "DELETE FROM notebook WHERE user_id = ?";
        String sql2 = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false); // 开启事务

            // 删除用户的所有笔记
            try (PreparedStatement pstmtNotes = conn.prepareStatement(sql1)) {
                pstmtNotes.setInt(1, userId);
                pstmtNotes.executeUpdate();
            }

            // 删除用户
            try (PreparedStatement pstmtUser = conn.prepareStatement(sql2)) {
                pstmtUser.setInt(1, userId);
                int affectedRows = pstmtUser.executeUpdate();

                if (affectedRows > 0) {
                    conn.commit(); // 提交事务
                    return true;
                } else {
                    conn.rollback(); // 回滚事务
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
