package server;

import clientManager.ClientManager;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private Map<String, Socket> clients;

    public Server() {
        clients = new HashMap<>();
    }

    public void startSession() {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            System.out.println("Server in ascolto sulla porta 3000\n----------------------------");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientManager clientManager = new ClientManager(clientSocket, this);
                new Thread(clientManager).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addClient(String username, Socket socket) {
        clients.put(username, socket);
    }

    public void removeClient(String username) {
        clients.remove(username);
    }

    public void sendBroadcastMessage(String sender, String message) {

        //creo una sotto hashmap formata da ogni client apparte il sender e mando il messaggio ad ogni client
        clients.entrySet().stream().filter(entry -> !entry.getKey().equals(sender)).forEach(entry -> {
            Socket socket = entry.getValue();
            try {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println(sender + ": " + message);
                printWriter.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }

}
