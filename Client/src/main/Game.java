package main;

import bean.Player;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

public class Game implements KeyListener {

    private GamePanel gamePanel;
    private Player player;
    private Player enemyPlayer;
    private String username;
    private PrintWriter printWriter;
    private Socket clientSocket;

    public Game(GamePanel gamePanel, String username, Socket clientSocket) {
        this.gamePanel = gamePanel;
        this.gamePanel.addKeyListener(this);
        player = new Player(100);
        enemyPlayer = new Player(600);
        this.username = username;
        this.clientSocket = clientSocket;
        try {
            printWriter = new PrintWriter(this.clientSocket.getOutputStream());
            printWriter.println(this.username);
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update() {
        player.update();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        printWriter.println(player.getPosY());
        printWriter.flush();
        String enemyPosY;
        try {
            enemyPosY = bufferedReader.readLine();
            System.out.println(enemyPosY);
            enemyPlayer.updateEnemy(Integer.parseInt(enemyPosY));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());
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
}
