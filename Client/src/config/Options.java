package config;

public class Options {

    private static Options instance = null;

    private final static int TILES_DEFAULT_SIZE = 32;
    private final static float SCALE = 1.5f;
    private final static int TILES_IN_WIDTH = 26;
    private final static int TILES_IN_HEIGHT = 14;
    private final static int TILE_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    private final static int GAME_WIDTH = TILE_SIZE * TILES_IN_WIDTH;
    private final static int GAME_HEIGHT = TILE_SIZE * TILES_IN_HEIGHT;

    private Options() {}

    public static Options getInstance() {
        return instance == null ? instance = new Options() : instance;
    }

    public static int getTilesDefaultSize() {
        return TILES_DEFAULT_SIZE;
    }

    public static float getSCALE() {
        return SCALE;
    }

    public static int getTilesInWidth() {
        return TILES_IN_WIDTH;
    }

    public static int getTilesInHeight() {
        return TILES_IN_HEIGHT;
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }

    public static int getGameWidth() {
        return GAME_WIDTH;
    }

    public static int getGameHeight() {
        return GAME_HEIGHT;
    }
}
