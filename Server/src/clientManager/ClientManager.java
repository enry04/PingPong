package clientManager;

import server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientManager implements Runnable{

    private Socket clientSocket;
    private Server server;
    private String username;

    public ClientManager(Socket clientSocket, Server server){
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            username = bufferedReader.readLine();
            server.addClient(username, clientSocket);
            String message = "";
            while((message = bufferedReader.readLine()) != null){
                System.out.println(username + ": " + message);
                server.sendBroadcastMessage(username, message);
            }
            server.removeClient(username);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
