package main;

import bean.Ball;
import bean.Player;
import config.Options;

import javax.xml.stream.events.EndElement;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
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
    private boolean canStart, ballOwner = false, isFirstTime = true, isFirstPlayer;
    private Ball ball;

    public Game(GamePanel gamePanel, Socket clientSocket, String username) {
        this.gamePanel = gamePanel;
        this.gamePanel.addKeyListener(this);
        this.username = username;
        player = new Player();
        ball = new Ball(Options.getInstance().getGameWidth() / 2 - Ball.getBallWidth() / 2, Options.getInstance().getGameHeight() / 2 - Ball.getBallHeight());
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
            output.println("/enemyPos " + player.getPosY());
            if (ballOwner) {
                if (isFirstTime) {
                    ball.setxDir(-1);
                    ball.setyDir(1);
                    isFirstTime = false;
                }
                if (ball.getyPos() + Ball.getBallHeight() <= 0 || ball.getyPos() >= Options.getGameHeight()) {
                    ball.setyDir(ball.getyDir() * -1);
                }
                if (ball.getxPos() + Ball.getBallWidth() <= 0 || ball.getxPos() >= Options.getGameWidth()) {
                    ball.setxDir(ball.getxDir() * -1);
                }
                /*if (new Rectangle(ball.getxPos(), ball.getyPos(), Ball.getBallWidth(), Ball.getBallHeight()).intersects(new Rectangle(player.getPosX(), player.getPosY(), Player.getPlayerWidth(), Player.getPlayerHeight()))) {
                    ball.setxDir(ball.getxDir() * -1);
                }
                if (new Rectangle(ball.getxPos(), ball.getyPos(), Ball.getBallWidth(), Ball.getBallHeight()).intersects(new Rectangle(enemyPlayer.getPosX(), enemyPlayer.getPosY(), Player.getPlayerWidth(), Player.getPlayerHeight()))) {
                    ball.setxDir(ball.getxDir() * -1);
                }*/
                //TODO: check every single line of rectangles
                ball.setyPos(ball.getyPos() + Ball.getBallSpeed() * ball.getyDir());
                ball.setxPos(ball.getxPos() + Ball.getBallSpeed() * ball.getxDir());
                output.println("/ballPos " + ball.getxPos() + " " + ball.getyPos());
            }
        }


    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
        if (canStart) {
            enemyPlayer.draw(g);
            player.draw(g);
            ball.drawBall(g);
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
                        } else if (message.contains("/start")) {
                            canStart = true;
                        } else if (message.contains(("/ball"))) {
                            ballOwner = true;
                        } else if (message.contains("/enemyPos")) {
                            enemyPlayer.updateEnemy(Integer.parseInt(message.split(" ")[1]));
                        } else if (message.contains("/ballPos") && !ballOwner) {
                            ball.setxPos(Integer.parseInt(message.split(" ")[1]));
                            ball.setyPos(Integer.parseInt(message.split(" ")[2]));
                        } else if (message.contains("/player")) {
                            if (message.split(" ")[1].contains("1")) {
                                isFirstPlayer = true;
                            } else {
                                isFirstPlayer = false;
                            }
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
