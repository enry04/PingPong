package server;

import bean.Ball;
import bean.Player;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server implements Runnable {
    private final static int GAME_WIDTH = 1248;
    private final static int GAME_HEIGHT = 800;
    private ServerSocket serverSocket;
    private Socket player1Socket, player2Socket;
    private PrintWriter player1Writer, player2Writer;
    private BufferedReader player1Reader, player2Reader;
    private String player1Username, player2Username;
    private Player player1, player2;
    private Ball ball;
    private Thread thread;
    private int firstPlayerPoints = 0, secondPlayerPoints = 0;

    public Server() {
    }

    public void listen() {
        try {
            serverSocket = new ServerSocket(3000);

            player1Socket = serverSocket.accept();

            player1Writer = new PrintWriter(player1Socket.getOutputStream(), true);
            player1Reader = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            player1Username = player1Reader.readLine();
            player1 = new Player(100, GAME_HEIGHT / 2 + Player.getPlayerHeight() / 2);
            System.out.println(player1Username + " connected.");
            player1Writer.println("/position 100 1148");

            player2Socket = serverSocket.accept();

            player2Writer = new PrintWriter(player2Socket.getOutputStream(), true);
            player2Reader = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            player2Username = player2Reader.readLine();
            player2 = new Player(1148, GAME_HEIGHT / 2 + Player.getPlayerHeight() / 2);
            System.out.println(player2Username + " connected.");
            player2Writer.println("/position 1148 100");

            player1Writer.println("/start");
            player2Writer.println("/start");

            spawnBall();
            thread = new Thread(this);
            thread.start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void spawnBall() {
        ball = new Ball(GAME_WIDTH / 2 - Ball.getBallWidth() / 2, GAME_HEIGHT / 2 - Ball.getBallHeight());
        boolean isGoLeft, isGoUp;
        Random random = new Random();
        isGoLeft = random.nextBoolean();
        isGoUp = random.nextBoolean();
        if (isGoLeft && isGoUp) {
            ball.setxDir(-1);
            ball.setyDir(-1);
        } else if (!isGoUp && !isGoLeft) {
            ball.setxDir(1);
            ball.setyDir(1);
        } else if (isGoUp) {
            ball.setxDir(1);
            ball.setyDir(-1);
        } else {
            ball.setxDir(-1);
            ball.setyDir(1);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String player1Mex = player1Reader.readLine();
                if (player1Mex.contains("/enemyPos")) {
                    player1.setPosY(Integer.parseInt(player1Mex.split(" ")[1]));
                    player2Writer.println(player1Mex);
                }
                player2Writer.flush();
                String player2Mex = player2Reader.readLine();
                if (player2Mex.contains("/enemyPos")) {
                    player2.setPosY(Integer.parseInt(player2Mex.split(" ")[1]));
                    player1Writer.println(player2Mex);
                }
                player1Writer.flush();
                ball.setyPos(ball.getyPos() + Ball.getBallSpeed() * ball.getyDir());
                ball.setxPos(ball.getxPos() + Ball.getBallSpeed() * ball.getxDir());

                if (ball.getyPos() + Ball.getBallHeight() <= 0 || ball.getyPos() >= GAME_HEIGHT) {
                    ball.setyDir(ball.getyDir() * -1);
                } else if (ball.getxPos() + Ball.getBallWidth() <= 0) {
                    secondPlayerPoints++;
                    player1Writer.println("/points " + firstPlayerPoints + " " + secondPlayerPoints);
                    player2Writer.println("/points " + secondPlayerPoints + " " + firstPlayerPoints);
                    spawnBall();
                } else if (ball.getxPos() >= GAME_WIDTH) {
                    firstPlayerPoints++;
                    player1Writer.println("/points " + firstPlayerPoints + " " + secondPlayerPoints);
                    player2Writer.println("/points " + secondPlayerPoints + " " + firstPlayerPoints);
                    spawnBall();
                } else if (new Rectangle(ball.getxPos(), ball.getyPos(), Ball.getBallWidth(), Ball.getBallHeight()).intersectsLine(player1.getPosX(), player1.getPosY(), player1.getPosX(), player1.getPosY() + Player.getPlayerHeight()) || new Rectangle(ball.getxPos(), ball.getyPos(), Ball.getBallWidth(), Ball.getBallHeight()).intersectsLine(player1.getPosX() + Player.getPlayerWidth(), player1.getPosY(), player1.getPosX() + Player.getPlayerWidth(), player1.getPosY() + Player.getPlayerHeight())) {
                    ball.setxDir(ball.getxDir() * -1);
                } else if (new Rectangle(ball.getxPos(), ball.getyPos(), Ball.getBallWidth(), Ball.getBallHeight()).intersectsLine(player1.getPosX(), player1.getPosY(), player1.getPosX() + Player.getPlayerWidth(), player1.getPosY()) || new Rectangle(ball.getxPos(), ball.getyPos(), Ball.getBallWidth(), Ball.getBallHeight()).intersectsLine(player1.getPosX(), player1.getPosY() + Player.getPlayerHeight(), player1.getPosX() + Player.getPlayerWidth(), player1.getPosY() + Player.getPlayerHeight())) {
                    ball.setxDir(ball.getyDir() * -1);
                } else if (new Rectangle(ball.getxPos(), ball.getyPos(), Ball.getBallWidth(), Ball.getBallHeight()).intersectsLine(player2.getPosX(), player2.getPosY(), player2.getPosX(), player2.getPosY() + Player.getPlayerHeight()) || new Rectangle(ball.getxPos(), ball.getyPos(), Ball.getBallWidth(), Ball.getBallHeight()).intersectsLine(player2.getPosX() + Player.getPlayerWidth(), player2.getPosY(), player2.getPosX() + Player.getPlayerWidth(), player2.getPosY() + Player.getPlayerHeight())) {
                    ball.setxDir(ball.getxDir() * -1);
                } else if (new Rectangle(ball.getxPos(), ball.getyPos(), Ball.getBallWidth(), Ball.getBallHeight()).intersectsLine(player2.getPosX(), player2.getPosY(), player2.getPosX() + Player.getPlayerWidth(), player2.getPosY()) || new Rectangle(ball.getxPos(), ball.getyPos(), Ball.getBallWidth(), Ball.getBallHeight()).intersectsLine(player2.getPosX(), player2.getPosY() + Player.getPlayerHeight(), player2.getPosX() + Player.getPlayerWidth(), player2.getPosY() + Player.getPlayerHeight())) {
                    ball.setxDir(ball.getyDir() * -1);
                }
                player1Writer.println("/ballPos " + ball.getxPos() + " " + ball.getyPos());
                player2Writer.println("/ballPos " + ball.getxPos() + " " + ball.getyPos());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
