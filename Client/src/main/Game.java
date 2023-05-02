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
    private String username;

    public Game(GamePanel gamePanel, String username) {
        this.gamePanel = gamePanel;
        this.gamePanel.addKeyListener(this);
        player = new Player();
        this.username = username;
    }

    public void update(Socket clientSocket) {
        player.update();
        try {
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
            printWriter.println(player.getPosY());
            printWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0, gamePanel.getWidth(), gamePanel.getHeight());
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
