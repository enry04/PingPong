package main;

import bean.Ball;
import bean.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

public class Game implements KeyListener {

    private GamePanel gamePanel;
    private PrintWriter output;
    private Socket clientSocket;
    private BufferedReader input;
    private String username;
    private Thread readThread;
    private Player player, enemyPlayer;
    private Ball ball;
    private boolean canStart = false;

    public Game(GamePanel gamePanel, Socket clientSocket, String username) {
        this.gamePanel = gamePanel;
        this.gamePanel.addKeyListener(this);
        this.clientSocket = clientSocket;
        this.username = username;
        player = new Player();
        enemyPlayer = new Player();
        ball = new Ball();
        connectPlayer();
        readThread = new Thread(new ReadThread());
        readThread.start();
    }

    private void connectPlayer() {
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            output.println(this.username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (canStart) {
            player.update();
            output.println("/enemyPos " + player.getPosY());
            output.flush();
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
        if (canStart) {
            enemyPlayer.draw(g);
            player.draw(g);
            g.fillOval(ball.getxPos(), ball.getyPos(), Ball.getBallWidth(), Ball.getBallHeight());
        }
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
                        } else if (message.equals("/start")) {
                            canStart = true;
                        } else if (message.contains("/enemyPos")) {
                            enemyPlayer.updateEnemy(Integer.parseInt(message.split(" ")[1]));
                        } else if (message.contains("/ballPos")) {
                            ball.setxPos(Integer.parseInt(message.split(" ")[1]));
                            ball.setyPos(Integer.parseInt(message.split(" ")[2]));
                        } else if (message.contains("/points")) {
                            System.out.println("I tuoi punti: " + message.split(" ")[1] + " -------- I punti dell' avversario: " + message.split(" ")[2]);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Un client si è disconnesso: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
                }
            }
        }
    }
}
