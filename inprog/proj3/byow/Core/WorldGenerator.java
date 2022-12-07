package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

public class WorldGenerator implements Serializable {
    private static class Bounds {
        int x1, y1, x2, y2;
        Bounds(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    private TETile[][] world;
    private final int WIDTH;
    private int HEIGHT;
    private boolean[][] usedTiles;
    static private final TETile grass = Tileset.GRASS;
    static private final TETile tree = Tileset.TREE;
    static private final TETile mountain = Tileset.MOUNTAIN;
    static private final TETile flower = Tileset.FLOWER;
    static private final TETile wall = Tileset.WALL;
    static private final TETile floor = Tileset.FLOOR;
    static private final TETile lockedDoor = Tileset.LOCKED_DOOR;
    static private final TETile unlockedDoor = Tileset.UNLOCKED_DOOR;
    static private final TETile player = Tileset.AVATAR;
    static private final TETile portal = Tileset.PORTAL;
    static TETile playerOn;
    private int playerX;
    private int playerY;

    WorldGenerator(TETile[][] world, int width, int height) {
        this.world = world;
        this.WIDTH = width;
        this.HEIGHT = height;
        resetUsedTiles();
    }

    private void resetUsedTiles() {
        this.usedTiles = new boolean[WIDTH][HEIGHT];
    }

    void generateBlankWorld(Random random) {
        int flowerOdds = random.nextInt(95) + 5;
        int treeOdds = random.nextInt(95) + 5;
        int mountainOdds = random.nextInt(95) + 5;
        for (int w = 0; w < WIDTH; w++) {
            for (int h = 0; h < HEIGHT; h++) {
                int rInt = random.nextInt(flowerOdds);
                if (rInt == 0) {
                    world[w][h] = flower;
                    continue;
                }
                rInt = random.nextInt(treeOdds);
                if (rInt == 0) {
                    world[w][h] = tree;
                    continue;
                }
                rInt = random.nextInt(mountainOdds);
                if (rInt == 0) {
                    world[w][h] = mountain;
                    continue;
                }
                world[w][h] = grass;
            }
        }
    }

    /**
     * Generates an entire world from scratch with a provided random.
     */
    void generateWorld(Random random) {
        resetUsedTiles();
        generateBlankWorld(random);
        generateSpawnRoom(random);
        int rooms = random.nextInt(18) + 4;
        for (int i = 0; i < rooms; i++) {
            try {
                generateRoom(random);
            } catch (StackOverflowError e) {
                return;
            }
        }
    }

    /**
     * Builds a spawn room and returns the tile under the player.
     * @param random  The Random instance being used by Engine.
     */
    private void generateSpawnRoom(Random random) {
        Bounds b = generateRoom(random);
        playerX = random.nextInt(b.x2 - b.x1 - 2) + b.x1 + 1;
        playerY = random.nextInt(b.y2 - b.y1 - 2) + b.y1 + 1;
        playerOn = world[playerX][playerY];
        world[playerX][playerY] = player;
    }

    private Bounds generateRoom(Random random) throws StackOverflowError {
        int w = random.nextInt(6) + 6;
        int h = random.nextInt(4) + 6;
        int x1 = random.nextInt(WIDTH - w);
        int y1 = random.nextInt(HEIGHT - h);
        int x2 = x1 + w;
        int y2 = y1 + h;
        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                if (usedTiles[i][j]) {
                    return generateRoom(random);
                }
            }
        }
        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                if (i == x1 || i == x2 - 1 || j == y1 || j == y2 - 1) {
                    world[i][j] = wall;
                } else {
                    world[i][j] = floor;
                }
                usedTiles[i][j] = true;
            }
        }
        Bounds b = new Bounds(x1, y1, x2, y2);
        return b;
    }
    void move(char c) {
        if (c == 'W') {
            moveTo(playerX, playerY + 1);
        } else if (c == 'A') {
            moveTo(playerX - 1, playerY);
        } else if (c == 'S') {
            moveTo(playerX, playerY - 1);
        } else {
            moveTo(playerX + 1, playerY);
        }
    }

    private void moveTo(int x, int y) {
        TETile t = world[x][y];
        if (t == floor || t == portal || t == unlockedDoor || t == lockedDoor) {
            world[playerX][playerY] = playerOn;
            playerOn = t;
            world[x][y] = player;
            playerX = x;
            playerY = y;
        }
    }
}
