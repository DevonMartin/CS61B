package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class WorldGenerator {
    public class Bounds {
        int x1, y1, x2, y2;
        Bounds(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    TETile[][] world;
    int WIDTH;
    int HEIGHT;
    boolean[][] usedTiles;
    TETile grass = Tileset.GRASS;
    TETile tree = Tileset.TREE;
    TETile mountain = Tileset.MOUNTAIN;
    TETile flower = Tileset.FLOWER;
    TETile wall = Tileset.WALL;
    TETile floor = Tileset.FLOOR;
    TETile player = Tileset.AVATAR;
    TETile portal = Tileset.PORTAL;
    TETile playerOn;

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
    void generateSpawnRoom(Random random) {
        Bounds b = generateRoom(random);
        int playerX = random.nextInt(b.x2 - b.x1 - 2) + b.x1 + 1;
        int playerY = random.nextInt(b.y2 - b.y1 - 2) + b.y1 + 1;
        playerOn = world[playerX][playerY];
        world[playerX][playerY] = player;
    }

    Bounds generateRoom(Random random) throws StackOverflowError {
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

}
