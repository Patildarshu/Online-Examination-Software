package result;

import java.sql.*;
import db.DBConnection;

public class ResultService {

    public void saveResult(int userId, int score) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO results(user_id, score) VALUES(?,?)"
            );
            ps.setInt(1, userId);
            ps.setInt(2, score);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean hasAttempted(int userId) {
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM results WHERE user_id=?"
            );
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if already attempted
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
