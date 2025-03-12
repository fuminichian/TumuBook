package dao;

import util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO
{
    public static boolean registerUser(String username, String password, String email)
    {
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
}
