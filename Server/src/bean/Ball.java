package bean;

public class Ball {

    private int xPos, yPos, xDir, yDir;
    private static final int BALL_SPEED = 2;
    private static final int BALL_WIDTH = 20, BALL_HEIGHT = 20;

    public Ball(int startX, int startY) {
        this.xPos = startX;
        this.yPos = startY;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getxDir() {
        return xDir;
    }

    public int getyDir() {
        return yDir;
    }

    public static int getBallSpeed() {
        return BALL_SPEED;
    }

    public void setxDir(int xDir) {
        this.xDir = xDir;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public void setyDir(int yDir) {
        this.yDir = yDir;
    }

    public static int getBallHeight() {
        return BALL_HEIGHT;
    }

    public static int getBallWidth() {
        return BALL_WIDTH;
    }
}
