package Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class QuizQuestionDAO {
    private Connection connection;

    public QuizQuestionDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Question> getQuestionsByCategory(String category, int numberOfQuestions) {
        List<Question> questions = new ArrayList<>();

        String query = "SELECT question, answer FROM quiz_questions WHERE category = ? ORDER BY RAND() LIMIT ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, category);
            preparedStatement.setInt(2, numberOfQuestions);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String questionText = resultSet.getString("question");
                    String answer = resultSet.getString("answer");
                    Question question = new Question(questionText, answer);
                    questions.add(question);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questions;
    }
}
