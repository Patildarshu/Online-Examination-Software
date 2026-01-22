package result;

import java.sql.*;
import db.DBConnection;

public class ResultService {

    public void saveResult(int userId, String subject, int score) {

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO results(user_id, subject, score) VALUES (?,?,?)"
            );

            ps.setInt(1, userId);
            ps.setString(2, subject);
            ps.setInt(3, score);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean hasAttempted(int userId, String subject) {

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM results WHERE user_id=? AND subject=?"
            );

            ps.setInt(1, userId);
            ps.setString(2, subject);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
