package dao;

import util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Note;
import java.util.List;
import java.util.ArrayList;

public class NoteDAO {
    public static boolean addNote(int userId, String title, String content) {
        String sql = "INSERT INTO notebook (user_id, title, content, create_time, update_time) VALUES (?, ?, ?, NOW(), NOW())";
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

    public static List<Note> getNotebookInfo(int userId) {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT id, title, content, create_time, update_time FROM notebook WHERE user_id = ? ORDER BY create_time DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {  // 遍历所有笔记
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String createTime = rs.getString("create_time");
                String updateTime = rs.getString("update_time");

                notes.add(new Note(id, title, content, createTime, updateTime));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notes;
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

    public static boolean deleteNote(int noteId) {
        String sql = "DELETE FROM notebook WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, noteId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // 如果删除成功返回 true，否则返回 false

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}
