package config;

public class Options {

    private static Options instance = null;

    private final static int GAME_WIDTH = 1248;
    private final static int GAME_HEIGHT = 800;

    private Options() {
    }

    public static Options getInstance() {
        return instance == null ? instance = new Options() : instance;
    }

    public static int getGameWidth() {
        return GAME_WIDTH;
    }

    public static int getGameHeight() {
        return GAME_HEIGHT;
    }
}
