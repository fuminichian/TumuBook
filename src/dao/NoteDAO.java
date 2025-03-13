package dao;

import util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NoteDAO {
    public static boolean addNote(int userId, String title, String content) {
        String sql = "INSERT INTO notes (user_id, title, content, create_time, update_time) VALUES (?, ?, ?, NOW(), NOW())";
        try(Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, userId);  // 用户ID
            pstmt.setString(2, title); // 笔记标题
            pstmt.setString(3, content); // 笔记内容
            int rowsAffected = pstmt.executeUpdate(); // 插入成功的行数
            return rowsAffected > 0;

        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("发生错误请重试");
        }
        return false;
    }

    public static String getNotebookInfo(int userId) {
        String sql = "SELECT title, content, create_time FROM notebook WHERE user_id = ? ORDER BY create_time DESC";
        StringBuilder result = new StringBuilder();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {  // 遍历所有笔记
                String title = rs.getString("title");
                String content = rs.getString("content");
                String createTime = rs.getString("create_time");

                result.append("标题: ").append(title)
                        .append("\n内容: ").append(content)
                        .append("\n创建时间: ").append(createTime)
                        .append("\n----------------------\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "查询笔记列表失败: " + e.getMessage();
        }

        return result.length() == 0 ? "没有找到笔记" : result.toString();
    }

    public static Boolean updateNote(int userId, String title, String content,int notebookId) {
        String sql = "UPDATE notebook SET title = ?, content = ?, update_time = CURRENT_TIMESTAMP WHERE id = ? AND user_id = ?";
        try(Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setInt(3, notebookId);
            pstmt.setInt(4, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }catch(SQLException e){
            e.printStackTrace();
            System.err.println("修改笔记失败: " + e.getMessage());
            return false;
        }
    }


}
