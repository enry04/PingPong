package bean;

import config.Options;

import java.awt.*;

public class Player {
    private final static int PLAYER_WIDTH = 25;
    private final static int PLAYER_HEIGHT = 75;
    private int posX;
    private int posY = Options.getInstance().getGameHeight() / 2 + PLAYER_HEIGHT / 2;
    private final static int SPEED = 5;
    private boolean isUp = false, isDown = false;

    public void update() {
        if (isUp) {
            if (posY >= SPEED - 2) {
                posY -= SPEED;
            }
        } else if (isDown) {
            if (posY + PLAYER_HEIGHT <= Options.getInstance().getGameHeight()) {
                posY += SPEED;
            }
        }
    }

    public void updateEnemy(int posY){
        this.posY = posY;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(posX, posY, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isUp() {
        return isUp;
    }

    public boolean isDown() {
        return isDown;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public void setDown(boolean down) {
        isDown = down;
    }
}
