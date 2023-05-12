package server;

import bean.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final static int GAME_WIDTH = 1248;
    private final static int GAME_HEIGHT = 800;
    private ServerSocket serverSocket;
    private Socket player1Socket, player2Socket;
    private PrintWriter player1Writer, player2Writer;
    private BufferedReader player1Reader, player2Reader;
    private String player1Username, player2Username;
    private Player player1, player2;
    private Thread thread;

    public Server() {

    }

    public void listen() {
        try {
            serverSocket = new ServerSocket(3000);

            player1Socket = serverSocket.accept();
            System.out.println("Player 1 connected.");

            player1Writer = new PrintWriter(player1Socket.getOutputStream(), true);
            player1Reader = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            player1Username = player1Reader.readLine();
            player1 = new Player(100, GAME_HEIGHT / 2 + Player.getPlayerHeight() / 2);
            player1Writer.println("/position 100 1148");

            player2Socket = serverSocket.accept();
            System.out.println("Player 2 connected.");

            player2Writer = new PrintWriter(player2Socket.getOutputStream(), true);
            player2Reader = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            player2Username = player2Reader.readLine();
            player2 = new Player(1148, GAME_HEIGHT / 2 + Player.getPlayerHeight() / 2);
            player2Writer.println("/position 1148 100");

            player1Writer.println("/start");
            player2Writer.println("/start");

            thread = new Thread(this);
            thread.start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
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
