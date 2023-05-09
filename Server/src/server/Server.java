package server;

import clientsManager.ClientsManager;

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

            player2Socket = serverSocket.accept();
            System.out.println("Player 2 connected.");

            player2Writer = new PrintWriter(player2Socket.getOutputStream(), true);
            player2Reader = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            player2Username = player2Reader.readLine();
            player2Writer.println("/position 1148 100");

            while(true){
                String player1Y = player1Reader.readLine();
                System.out.println("Received message from player 1: " + player1Y);
                player2Writer.println(player1Y);

                String player2Y = player2Reader.readLine();
                System.out.println("Received message from player 2: " + player2Y);
                player1Writer.println(player2Y);
            }

            //new ClientsManager(clientSocket, this).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClient(Socket socket, String username) {
        clients.put(username, socket);
    }

    public Map<String, Socket> getClients() {
        return clients;
    }
}
