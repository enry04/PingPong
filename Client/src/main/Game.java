package main;

import bean.Player;
import config.Options;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

public class Game implements KeyListener {

    private GamePanel gamePanel;
    private Player player;
    private PrintWriter output;
    private Socket clientSocket;
    private BufferedReader input;
    private Thread readThread;
    private Player enemyPlayer;
    private String username;
    private boolean canStart;

    public Game(GamePanel gamePanel, Socket clientSocket, String username) {
        this.gamePanel = gamePanel;
        this.gamePanel.addKeyListener(this);
        this.username = username;
        player = new Player();
        enemyPlayer = new Player();
        this.clientSocket = clientSocket;
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(this.clientSocket.getOutputStream(), true);
            output.println(this.username);
            readThread = new Thread(new ReadThread());
            readThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update() {
        if (canStart) {
            player.update();
            output.println(player.getPosY());
        }

    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
        enemyPlayer.draw(g);
        player.draw(g);
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                player.setUp(true);
                player.setDown(false);
                break;
            case KeyEvent.VK_S:
                player.setUp(false);
                player.setDown(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                player.setUp(false);
                break;
            case KeyEvent.VK_S:
                player.setDown(false);
                break;
        }
    }

    private class ReadThread implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    String message = input.readLine();
                    if (message != null) {
                        if (message.contains("/position")) {
                            player.setPosX(Integer.parseInt(message.split(" ")[1]));
                            enemyPlayer.setPosX(Integer.parseInt(message.split(" ")[2]));
                        } else if (message.contains("/start")) {
                            canStart = true;
                        } else {
                            enemyPlayer.updateEnemy(Integer.parseInt(message));
                        }
                        System.out.println("Messaggio ricevuto dal server: " + message);
                    }
                }
            } catch (IOException e) {
                System.err.println("Errore durante la lettura dei messaggi dal server: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
                }
            }
        }
    }

    public boolean isCanStart() {
        return canStart;
    }


}
