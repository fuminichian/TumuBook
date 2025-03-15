package service;

import dao.NoteDAO;
import dao.UserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import model.Note;
import util.DBUtil;
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

public class NoteService {
    public static String addNote(int userId, String title, String content) {
        boolean result = NoteDAO.addNote(userId, title, content);
        if (result) {
            return "笔记添加成功";
        }
        else{
            return "笔记添加失败";
        }

    }

    public static List<Note> searchNotesByKeywordOrTag(int userId, String keyword) {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT DISTINCT n.id, n.title, n.content, n.create_time, n.update_time " +
                "FROM notebook n " +
                "LEFT JOIN note_tags nt ON n.id = nt.note_id " +
                "LEFT JOIN tags t ON nt.tag_id = t.id " +
                "WHERE n.user_id = ? AND (n.title LIKE ? OR n.content LIKE ? OR t.name LIKE ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, "%" + keyword + "%");
            pstmt.setString(3, "%" + keyword + "%");
            pstmt.setString(4, "%" + keyword + "%");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // 获取笔记的基本信息
                int noteId = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String createTime = rs.getString("create_time");
                String updateTime = rs.getString("update_time");

                // 获取与该笔记相关的标签
                List<Tag> tags = TagDAO.getTagsForNote(noteId);

                // 创建 Note 对象，并传入标签列表
                Note note = new Note(noteId, title, content, createTime, updateTime, tags);
                notes.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notes;
    }







//    public static String getnotebookInfo(String username) {
//        int userId = UserDAO.getUserID(username);
//        return NoteDAO.getNotebookInfo(userId);
//    }

//    public static String updateNote(int userId, String title, String content) {
//        if (NoteDAO.updateNote(userId,title,content,notebookId)){
//            return "笔记修改成功";
//        }
//        else{
//            return "笔记修改失败";
//        }
//    }

}
