package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class WorldGenerator implements Serializable {
    /**
     * Represents the bounds a room encompasses.
     */
    private static class Bounds {
        int x1, y1, x2, y2;
        Bounds(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    /**
     * Extension of Bounds, also representing the location of
     * the room's door.
     */
    private static class Room {
        Bounds b;
        int doorX;
        int doorY;
        Room(Bounds b, int doorX, int doorY) {
            this.b = b;
            this.doorX = doorX;
            this.doorY = doorY;
        }
    }

    Engine engine;
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
    TETile playerOn;
    private int playerX;
    private int playerY;

    WorldGenerator(Engine engine) {
        this.engine = engine;
        resetUsedTiles();
    }

    private ArrayList<String> walkableTileDescriptions() {
        ArrayList<String> l = new ArrayList<>();
        l.add(lockedDoor.description());
        l.add(unlockedDoor.description());
        l.add(floor.description());
        l.add(portal.description());
        l.add(grass.description());
        l.add(flower.description());
        return l;
    }

    private void resetUsedTiles() {
        this.usedTiles = new boolean[Engine.WIDTH][Engine.HEIGHT];
    }

    void generateBlankWorld(Random random) {
        int flowerOdds = random.nextInt(95) + 5;
        int treeOdds = random.nextInt(95) + 5;
        int mountainOdds = random.nextInt(95) + 5;
        for (int w = 0; w < Engine.WIDTH; w++) {
            for (int h = 0; h < Engine.HEIGHT; h++) {
                int rInt = random.nextInt(flowerOdds);
                if (rInt == 0) {
                    engine.world[w][h] = flower;
                    continue;
                }
                rInt = random.nextInt(treeOdds);
                if (rInt == 0) {
                    engine.world[w][h] = tree;
                    continue;
                }
                rInt = random.nextInt(mountainOdds);
                if (rInt == 0) {
                    engine.world[w][h] = mountain;
                    continue;
                }
                engine.world[w][h] = grass;
            }
        }
    }

    /**
     * Generates an entire world from scratch with a provided random.
     */
    void generateWorld(Random random) {
        resetUsedTiles();
        generateBlankWorld(random);
        Room lastRoom = generateSpawnRoom(random);
        int rooms = random.nextInt(18) + 4;
        for (int i = 0; i < rooms; i++) {
            try {
                Bounds b = getNewRoomBounds(random);
                Room nextRoom = generateRoomAt(b, random);
//                connectRooms(lastRoom, nextRoom);
            } catch (StackOverflowError e) {
                return;
            }
        }
    }

    /**
     * Builds a spawn room and returns the tile under the player.
     * @param random  The Random instance being used by Engine.
     */
    private Room generateSpawnRoom(Random random) {
        Bounds b = getNewRoomBounds(random);
        Room room = generateRoomAt(b, random);
        playerX = random.nextInt(b.x2 - b.x1 - 2) + b.x1 + 1;
        playerY = random.nextInt(b.y2 - b.y1 - 2) + b.y1 + 1;
        playerOn = engine.world[playerX][playerY];
        engine.world[playerX][playerY] = player;
        return room;
    }

    private Bounds getNewRoomBounds(Random random) throws StackOverflowError {
        int w = random.nextInt(6) + 6;
        int h = random.nextInt(4) + 6;
        int x1 = random.nextInt(Engine.WIDTH - w - 4) + 2;
        int y1 = random.nextInt(Engine.HEIGHT - h - 4) + 2;
        int x2 = x1 + w;
        int y2 = y1 + h;
        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                if (usedTiles[i][j]) {
                    return getNewRoomBounds(random);
                }
            }
        }
        return new Bounds(x1, y1, x2, y2);
    }

    private Room generateRoomAt(Bounds b, Random random) {
        int w = b.x2 - b.x1;
        int h = b.y2 - b.y1;
        int walls = w * 2 + h * 2 - 4;
        ArrayList<Integer> corners = new ArrayList<>();
        corners.add(0);
        corners.add(h - 1);
        corners.add(walls - h);
        System.out.println(corners);
        int door = 0;
        while (corners.contains(door)) {
            door = random.nextInt(walls - 1);
        }
        System.out.println(door);
        int checkedWalls = 0;
        Room r = null;
        for (int i = b.x1; i < b.x2; i++) {
            for (int j = b.y1; j < b.y2; j++) {
                if (i == b.x1 || i == b.x2 - 1 || j == b.y1 || j == b.y2 - 1) {
                    if (checkedWalls == door) {
                        engine.world[i][j] = unlockedDoor;
                        r = new Room(b, i, j);
                    } else {
                        engine.world[i][j] = wall;
                    }
                    checkedWalls++;
                } else {
                    engine.world[i][j] = floor;
                }
                usedTiles[i][j] = true;
            }
        }
        return r;
    }
    void move(char c) {
        if (c == 'W') {
            moveTo(playerX, playerY + 1);
        } else if (c == 'A') {
            moveTo(playerX - 1, playerY);
        } else if (c == 'S') {
            moveTo(playerX, playerY - 1);
        } else if (c == 'D') {
            moveTo(playerX + 1, playerY);
        }
    }

    private void moveTo(int x, int y) {
        TETile t = engine.world[x][y];
        String d = t.description();
        if (walkableTileDescriptions().contains(d)) {
            engine.world[playerX][playerY] = playerOn;
            playerOn = t;
            engine.world[x][y] = player;
            playerX = x;
            playerY = y;
        }
    }
}
