package Klient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class QuizClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public QuizClient(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {

            sendPlayRequest();

            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Received from server: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendPlayRequest() {
        out.println("PLAY");
    }

    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 5555;
        QuizClient quizClient = new QuizClient(serverAddress, port);
        quizClient.start();
    }
}
