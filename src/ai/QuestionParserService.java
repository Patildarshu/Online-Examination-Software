package ai;

import com.google.gson.*;
import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class QuestionParserService {

    public void saveQuestions(String aiResponse) {

        try {
            JsonArray questions = JsonParser
                    .parseString(aiResponse)
                    .getAsJsonArray(); // ✅ FIX HERE

            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO questions(question,option1,option2,option3,option4,correct_option) VALUES(?,?,?,?,?,?)"
            );

            for (JsonElement q : questions) {
                JsonObject obj = q.getAsJsonObject();

                ps.setString(1, obj.get("question").getAsString());

                JsonArray opts = obj.getAsJsonArray("options");
                ps.setString(2, opts.get(0).getAsString());
                ps.setString(3, opts.get(1).getAsString());
                ps.setString(4, opts.get(2).getAsString());
                ps.setString(5, opts.get(3).getAsString());

                ps.setInt(6, obj.get("answer").getAsInt());

                ps.executeUpdate();
            }

            System.out.println("✅ AI Questions Saved Successfully!");

        } catch (Exception e) {
            System.out.println("❌ Failed to save AI questions");
            e.printStackTrace();
        }
    }
}
