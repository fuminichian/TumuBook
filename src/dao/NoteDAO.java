package dao;

import util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Note;
import java.util.List;
import java.util.ArrayList;
import model.Tag;
import dao.TagDAO;
import java.sql.Statement;

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

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String createTime = rs.getString("create_time");
                String updateTime = rs.getString("update_time");

                // 获取该笔记的所有标签
                List<Tag> tags = TagDAO.getTagsForNote(id);

                // 创建笔记对象并加入列表
                notes.add(new Note(id, title, content, createTime, updateTime, tags));
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

    public static void addTagToNote(int noteId, String tagName) {
        // 首先检查标签是否已经存在
        String selectSql = "SELECT id FROM tags WHERE name = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement selectPstmt = conn.prepareStatement(selectSql)) {
            selectPstmt.setString(1, tagName);
            ResultSet rs = selectPstmt.executeQuery();
            int tagId = -1;
            if (rs.next()) {
                tagId = rs.getInt("id");
            } else {
                // 如果标签不存在，则插入新标签
                String insertTagSql = "INSERT INTO tags (name) VALUES (?)";
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertTagSql, Statement.RETURN_GENERATED_KEYS)) {
                    insertPstmt.setString(1, tagName);
                    insertPstmt.executeUpdate();
                    rs = insertPstmt.getGeneratedKeys();
                    if (rs.next()) {
                        tagId = rs.getInt(1);
                    }
                }
            }
            // 将标签和笔记关联
            if (tagId != -1) {
                String insertNoteTagSql = "INSERT INTO note_tags (note_id, tag_id) VALUES (?, ?)";
                try (PreparedStatement insertNoteTagPstmt = conn.prepareStatement(insertNoteTagSql)) {
                    insertNoteTagPstmt.setInt(1, noteId);
                    insertNoteTagPstmt.setInt(2, tagId);
                    insertNoteTagPstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT id, name FROM tags";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                tags.add(new Tag(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }




    public static List<Tag> getTagsForNote(int noteId) {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT t.id, t.name FROM tags t " +
                "JOIN note_tags nt ON t.id = nt.tag_id " +
                "WHERE nt.note_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, noteId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                tags.add(new Tag(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

//    public static Note getNoteWithTags(int userId) {
//        // 获取笔记信息
//        Note note = getNotebookInfo(userId);
//        if (note == null) return null; // 防止空指针异常
//
//        // 获取该笔记的所有标签
//        List<Tag> tags = TagDAO.getTagsForNote(userId);
//
//        // 将标签传递给 Note
//        return new Note(note.getId(), note.getTitle(), note.getContent(), note.getCreateTime(), note.getUpdateTime(), tags);
//    }







}
