package main;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameManager implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private Game game;
    private static final int FPS_SET = 120;
    private static final int UPS_SET = 200;
    private Socket clientSocket;
    private String username;

    public GameManager() {
        Scanner scanner = new Scanner(System.in);
        username = scanner.nextLine();
        try {
            clientSocket = new Socket("localhost", 3000);
            gamePanel = new GamePanel(this);
            game = new Game(gamePanel, username);
            gameWindow = new GameWindow(gamePanel);
            gamePanel.setFocusable(true);
            gamePanel.requestFocus();
            startLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();
        long previousTime = System.nanoTime();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }
            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " || UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    private void update() {
        game.update(clientSocket);
    }

    public void render(Graphics g) {
        game.draw(g);
    }

}
