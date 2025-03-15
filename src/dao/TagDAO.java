package dao;

import model.Tag;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DBUtil;
import model.Note;

public class TagDAO {



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
                int tagId = rs.getInt("id");
                String tagName = rs.getString("name");
                tags.add(new Tag(tagId, tagName));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tags;
    }
}


