package clientsManager;

import server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ClientsManager extends Thread {

    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private Server server;
    private String username;

    public ClientsManager(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            username = input.readLine();
            server.addClient(clientSocket, username);
            if(server.getClients().size() == 1){
                output.println("/position 100 1148");
            }else {
                output.println("/position 1148 100");
            }
            while (true) {
                String message = input.readLine();
                if (message == null) {
                    break;
                }
                System.out.println("messaggio ricevuto da " + username);
                System.out.println(message);
                for (Map.Entry<String, Socket> set : server.getClients().entrySet()) {
                    if (set.getKey() != username) {
                        output = new PrintWriter(set.getValue().getOutputStream(), true);
                        output.println(message);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.getClients().remove(clientSocket);
            try {
                clientSocket.close();
                System.out.println("Connessione chiusa con " + username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
