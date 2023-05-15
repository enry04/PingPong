package bean;

import java.awt.*;

public class Ball {

    private int xPos, yPos;
    private static final int BALL_WIDTH = 20, BALL_HEIGHT = 20;

    public Ball() {
    }

    public int getxPos() {
        return xPos;
    }
    public int getyPos() {
        return yPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public static int getBallWidth() {
        return BALL_WIDTH;
    }

    public static int getBallHeight() {
        return BALL_HEIGHT;
    }
}
