package main;

import config.Options;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private GameManager gameManager;

    public GamePanel(GameManager gameManager) {
        this.gameManager = gameManager;
        Dimension size = new Dimension(Options.getInstance().getGameWidth(), Options.getInstance().getGameHeight());
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        gameManager.render(g);
    }
}
