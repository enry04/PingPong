package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private ServerSocket serverSocket;
    private Map<String, Socket> clients;
    private Socket player1Socket, player2Socket;
    private PrintWriter player1Writer, player2Writer;
    private BufferedReader player1Reader, player2Reader;
    private String player1Username, player2Username;

    public Server() {
        clients = new HashMap<>();
    }

    public void listen() {
        try {
            serverSocket = new ServerSocket(3000);

            player1Socket = serverSocket.accept();
            System.out.println("Player 1 connected.");

            player1Writer = new PrintWriter(player1Socket.getOutputStream(), true);
            player1Reader = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            player1Username = player1Reader.readLine();
            player1Writer.println("/position 100 1148");
            player1Writer.println("/player 1");

            player2Socket = serverSocket.accept();
            System.out.println("Player 2 connected.");

            player2Writer = new PrintWriter(player2Socket.getOutputStream(), true);
            player2Reader = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            player2Username = player2Reader.readLine();
            player2Writer.println("/position 1148 100");
            player2Writer.println("/player 2");

            player1Writer.println("/start");
            player2Writer.println("/start");
            player1Writer.println("/ball");
//            player1Writer.flush();
//            player2Writer.flush();

            while (true) {

                String player1Y = player1Reader.readLine();
                System.out.println("Received message from " + player1Username + ": " + player1Y);
                player2Writer.println(player1Y);
                player2Writer.flush();

                String player2Y = player2Reader.readLine();
                System.out.println("Received message from " + player2Username + ": " + player2Y);
                player1Writer.println(player2Y);
                player1Writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
