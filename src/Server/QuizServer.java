package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class QuizServer {
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;

    public QuizServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clients = new ArrayList<>();
            System.out.println("Server listening on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListening() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());


                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startGame(ClientHandler player1, ClientHandler player2) {

        player1.sendMessage("Game is starting! Get ready!");
        player2.sendMessage("Game is starting! Get ready!");


    }

    public static void main(String[] args) {
        int port = 5555;
        QuizServer quizServer = new QuizServer(port);
        quizServer.startListening();
    }

    public List<ClientHandler> getClients() {
        return clients;
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private QuizServer quizServer;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket, QuizServer quizServer) {
        this.clientSocket = clientSocket;
        this.quizServer = quizServer;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received from client: " + inputLine);


                if (inputLine.equals("PLAY")) {
                    handlePlayRequest();
                }


            }


            System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostAddress());
            quizServer.getClients().remove(this);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private void handlePlayRequest() {

        List<ClientHandler> clients = quizServer.getClients();
        if (clients.size() >= 2) {
            ClientHandler player1 = clients.get(clients.size() - 2);
            ClientHandler player2 = clients.get(clients.size() - 1);


            clients.remove(player1);
            clients.remove(player2);


            quizServer.startGame(player1, player2);
        } else {

            sendMessage("Waiting for another player to join...");
        }
    }
}
