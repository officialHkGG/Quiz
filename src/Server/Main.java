package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class QuizApp {
    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quizdb", "username", "password")) {
            QuizQuestionDAO quizQuestionDAO = new QuizQuestionDAO(connection);


            List<Question> questions = quizQuestionDAO.getQuestionsByCategory("General Knowledge", 5);

            for (Question question : questions) {
                System.out.println("Question: " + question.getQuestion());
                System.out.println("Answer: " + question.getAnswer());
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
