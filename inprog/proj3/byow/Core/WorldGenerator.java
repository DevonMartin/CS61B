package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.*;

public class WorldGenerator implements Serializable {

    static class Coords implements Serializable {
        int x, y;
        Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Coords)) {
                return false;
            }
            Coords c = (Coords) o;
            return (c.x == this.x && c.y == this.y);
        }

        @Override
        public int hashCode() {
            return (this.x + 1) * 1000 + (this.y + 1) * 10000;
        }
    }

    static class Node {
        Coords c;
        Node parent = null;
        Node(Coords c) {
            this.c = c;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Node)) {
                return false;
            }
            Node n = (Node) o;
            return (n.c.equals(this.c));
        }

        @Override
        public int hashCode() {
            return c.hashCode();
        }
    }
    /**
     * Extension of Coords. Represents the bounds a room encompasses.
     */
    private static class Bounds {
        Coords c1, c2;
        Bounds(Coords c1, Coords c2) {
            this.c1 = c1;
            this.c2 = c2;
        }

        ArrayList<Coords> getCorners() {
            ArrayList<Coords> corners = new ArrayList<>();
            corners.add(new Coords(c1.x, c1.y));
            corners.add(new Coords(c1.x, c2.y - 1));
            corners.add(new Coords(c2.x - 1, c1.y));
            corners.add(new Coords(c2.x - 1, c2.y - 1));
            return corners;
        }

        boolean isWall(Coords c) {
            return c.x == this.c1.x || c.x == this.c2.x - 1 || c.y == this.c1.y || c.y == this.c2.y - 1;
        }
    }

    /**
     * Extension of Bounds, also representing the location of
     * the room's door.
     */
    static class Room {
        Bounds b;
        Coords doorCoords;
        Room(Bounds b, Coords doorCoords) {
            this.b = b;
            this.doorCoords = doorCoords;
        }

        ArrayList<Coords> getCorners() {
            return b.getCorners();
        }
    }

    Engine engine;
    ArrayList<Coords> usedTiles;
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

    /**
     * Returns and ArrayList of descriptions of all Tiles
     * that can be walked on by a player.
     */
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

    static ArrayList<String> replaceableTileDescriptions() {
        ArrayList<String> l = new ArrayList<>();
        l.add(grass.description());
        l.add(flower.description());
        l.add(lockedDoor.description());
        l.add(unlockedDoor.description());
        return l;
    }

    /**
     * Resets the usedTiles ArrayList for generating a new world.
     */
    private void resetUsedTiles() {
        this.usedTiles = new ArrayList<>();
    }

    /**
     * Randomly generates a background for a new world. No rooms
     * included.
     */
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
        Room nextRoom = null;
        int rooms = random.nextInt(4) + 2;
        for (int i = 0; i < rooms; i++) {
            try {
                Bounds b = getNewRoomBounds(random);
                nextRoom = generateRoomAt(b, random);
                if (nextRoom == null) {
                    return;
                }
                RoomConnector.connectRooms(nextRoom, lastRoom, engine.world, random, this);
            } catch (StackOverflowError e) {
                return;
            }
            lastRoom = addDoor(lastRoom, nextRoom, random);
        }
        addPortal(random);
    }

    /**
     * Builds a spawn room and returns the tile under the player.
     * @param random  The Random instance being used by Engine.
     */
    private Room generateSpawnRoom(Random random) {
        Bounds b = getNewRoomBounds(random);
        Room room = generateRoomAt(b, random);
        playerX = random.nextInt(b.c2.x - b.c1.x - 2) + b.c1.x + 1;
        playerY = random.nextInt(b.c2.y - b.c1.y - 2) + b.c1.y + 1;
        playerOn = engine.world[playerX][playerY];
        engine.world[playerX][playerY] = player;
        return room;
    }

    /**
     * Randomly creates a new, valid set of bounds for a room.
     * @throws StackOverflowError if valid bounds cannot be found.
     */
    private Bounds getNewRoomBounds(Random random) throws StackOverflowError {
        int w = random.nextInt(6) + 6;
        int h = random.nextInt(4) + 6;
        int x1 = random.nextInt(Engine.WIDTH - w - 4) + 2;
        int y1 = random.nextInt(Engine.HEIGHT - h - 4) + 2;
        int x2 = x1 + w;
        int y2 = y1 + h;
        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                if (usedTiles.contains(new Coords(i, j))) {
                    return getNewRoomBounds(random);
                }
            }
        }
        return new Bounds(new Coords(x1, y1), new Coords(x2, y2));
    }

    /**
     * Creates a room within pre-checked bounds. Returns a new Room.
     */
    Room generateRoomAt(Bounds b, Random random) {
        int w = b.c2.x - b.c1.x;
        int h = b.c2.y - b.c1.y;
        int walls = w * 2 + h * 2 - 4;
        int door = random.nextInt(walls - 1);
        int checkedWalls = 0;
        Room r = null;
        for (int i = b.c1.x; i < b.c2.x; i++) {
            for (int j = b.c1.y; j < b.c2.y; j++) {
                Coords c = new Coords(i, j);
                if (b.isWall(c)) {
                    String d = engine.world[i][j].description();
                    if (d.equals("door")) {
                        continue;
                    }
                    if (checkedWalls == door) {
                        if (!(b.getCorners().contains(c))) {
                            engine.world[i][j] = unlockedDoor;
                            r = new Room(b, new Coords(i, j));
                        } else {
                            door++;
                            engine.world[i][j] = wall;
                        }
                    } else {
                        engine.world[i][j] = wall;
                    }
                    checkedWalls++;
                } else {
                    if (engine.world[i][j] == player) {
                        continue;
                    }
                    engine.world[i][j] = floor;
                }
                usedTiles.add(new Coords(i, j));
            }
        }
        return r;
    }


    private Room addDoor(Room r1, Room r2, Random random) {
        Room r = random.nextInt(2) == 0 ? r1 : r2;
        Bounds b = r.b;
        int w = b.c2.x - b.c1.x;
        int h = b.c2.y - b.c1.y;
        int walls = w * 2 + h * 2 - 4;
        int door = random.nextInt(walls - 1);
        int checkedWalls = 0;
        for (int i = b.c1.x; i < b.c2.x; i++) {
            for (int j = b.c1.y; j < b.c2.y; j++) {
                Coords c = new Coords(i, j);
                if (door == checkedWalls) {
                    String d = engine.world[i][j].description();
                    if (d.equals("door") || b.getCorners().contains(c)) {
                        return addDoor(r1, r2, random);
                    } else {
                        for (int x = i - 1; x <= i + 1; x++) {
                            for (int y = j - 1; y <= j + 1; j++) {
                                String desc = engine.world[x][y].description();
                                if (desc.equals("grass") || desc.equals("flower")) {
                                    engine.world[i][j] = unlockedDoor;
                                    r.doorCoords = c;
                                    return r;
                                }
                            }
                        }
                        return addDoor(r1, r2, random);
                    }
                }
                if (b.isWall(c)) {
                    checkedWalls++;
                }
            }
        }
        return addDoor(r1, r2, random);
    }

    private void addPortal(Random r) {
        while (true) {
            int x = r.nextInt(Engine.WIDTH);
            int y = r.nextInt(Engine.HEIGHT);
            String d = engine.world[x][y].description();
            if (!d.equals("wall") && !d.equals("door")) {
                engine.world[x][y] = portal;
                return;
            }
        }
    }

    /**
     * Validates a move, and moves the player if valid.
     * @param c  The movement character input by the user.
     */
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

    /**
     * Moves the character to the x and y coordinate provided.
     */
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
