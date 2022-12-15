package byow.Core;


import java.io.Serializable;
import java.util.*;

import static byow.Core.Engine.*;
import static byow.Core.RoomConnector.getPath;

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
    }

    Engine engine;
    ArrayList<Coords> usedTiles;
    int playerOn;
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
    private ArrayList<Integer> walkableTileIds() {
        ArrayList<Integer> l = new ArrayList<>();
        l.add(lockedDoor);
        l.add(unlockedDoor);
        l.add(floor);
        l.add(portal);
        l.add(grass);
        l.add(flower);
        return l;
    }

    static ArrayList<Integer> replaceableTileIds() {
        ArrayList<Integer> l = new ArrayList<>();
        l.add(lockedDoor);
        l.add(unlockedDoor);
        l.add(grass);
        l.add(flower);
        l.add(floor);
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
    void generateBlankWorld() {
        int flowerOdds = engine.random.nextInt(95) + 5;
        int treeOdds = engine.random.nextInt(95) + 5;
        int mountainOdds = engine.random.nextInt(95) + 5;
        for (int w = 0; w < Engine.WIDTH; w++) {
            engine.worldIds[w][HEIGHT] = Engine.menu;
            for (int h = 0; h < Engine.HEIGHT; h++) {
                int rInt = engine.random.nextInt(flowerOdds);
                if (rInt == 0) {
                    engine.worldIds[w][h] = flower;
                    continue;
                }
                rInt = engine.random.nextInt(treeOdds);
                if (rInt == 0) {
                    engine.worldIds[w][h] = tree;
                    continue;
                }
                rInt = engine.random.nextInt(mountainOdds);
                if (rInt == 0) {
                    engine.worldIds[w][h] = mountain;
                    continue;
                }
                engine.worldIds[w][h] = grass;
            }
        }
    }

    /**
     * Generates an entire world from scratch with the engine's random.
     */
    void generateWorld() {
        resetUsedTiles();
        generateBlankWorld();
        Room lastRoom = generateSpawnRoom();
        Room nextRoom;
        int rooms = engine.random.nextInt(10) + 2;
        for (int i = 0; i < rooms; i++) {
            try {
                Bounds b = getNewRoomBounds();
                nextRoom = generateRoomAt(b);
                if (nextRoom == null || lastRoom == null) {
                    break;
                }
                RoomConnector.connectRooms(nextRoom, lastRoom, engine.worldIds, engine.random, this);
                lastRoom = addDoor(lastRoom, nextRoom);
            } catch (StackOverflowError ignored) {
                break;
            }
        }
        addPortal();
    }

    /**
     * Builds a spawn room and returns it.
     */
    private Room generateSpawnRoom() {
        Bounds b = getNewRoomBounds();
        Room room = generateRoomAt(b);
        playerX = engine.random.nextInt(b.c2.x - b.c1.x - 2) + b.c1.x + 1;
        playerY = engine.random.nextInt(b.c2.y - b.c1.y - 2) + b.c1.y + 1;
        playerOn = engine.worldIds[playerX][playerY];
        engine.worldIds[playerX][playerY] = player;
        return room;
    }

    /**
     * Randomly creates a new, valid set of bounds for a room.
     * @throws StackOverflowError if valid bounds cannot be found.
     */
    private Bounds getNewRoomBounds() throws StackOverflowError {
        int w = engine.random.nextInt(9) + 4;
        int h = engine.random.nextInt(7) + 4;
        int x1 = engine.random.nextInt(Engine.WIDTH - w - 4) + 2;
        int y1 = engine.random.nextInt(Engine.HEIGHT - h - 4) + 2;
        int x2 = x1 + w;
        int y2 = y1 + h;
        for (int i = x1; i < x2; i++) {
            for (int j = y1; j < y2; j++) {
                if (usedTiles.contains(new Coords(i, j))) {
                    return getNewRoomBounds();
                }
            }
        }
        return new Bounds(new Coords(x1, y1), new Coords(x2, y2));
    }

    /**
     * Creates a room within pre-checked bounds. Returns a new Room.
     */
    Room generateRoomAt(Bounds b) {
        int w = b.c2.x - b.c1.x;
        int h = b.c2.y - b.c1.y;
        int walls = w * 2 + h * 2 - 4;
        int door = engine.random.nextInt(walls - 1);
        int checkedWalls = 0;
        Room r = null;
        for (int x = b.c1.x - 2; x < b.c2.x + 2; x++) {
            for (int y = b.c1.y - 2; y < b.c2.y + 2; y++) {
                int d = engine.worldIds[x][y];
                if (d == mountain || d == tree) {
                    engine.worldIds[x][y] = grass;
                }
            }
        }
        for (int i = b.c1.x; i < b.c2.x; i++) {
            for (int j = b.c1.y; j < b.c2.y; j++) {
                Coords c = new Coords(i, j);
                if (b.isWall(c)) {
                    int d = engine.worldIds[i][j];
                    if (d == lockedDoor || d == unlockedDoor) {
                        continue;
                    }
                    if (checkedWalls == door) {
                        if (validateDoorLocation(c, b)) {
                            engine.worldIds[i][j] = unlockedDoor;
                            r = new Room(b, new Coords(i, j));
                        } else {
                            door++;
                            engine.worldIds[i][j] = wall;
                        }
                    } else {
                        engine.worldIds[i][j] = wall;
                    }
                    checkedWalls++;
                } else if (engine.worldIds[i][j] != player) {
                    engine.worldIds[i][j] = floor;
                }
                usedTiles.add(new Coords(i, j));
            }
        }
        return r;
    }


    private Room addDoor(Room r1, Room r2) throws StackOverflowError {
        Room r = engine.random.nextInt(2) == 0 ? r1 : r2;
        Bounds b = r.b;
        int w = b.c2.x - b.c1.x;
        int h = b.c2.y - b.c1.y;
        int walls = w * 2 + h * 2 - 4;
        int door = engine.random.nextInt(walls - 1);
        int checkedWalls = 0;
        for (int i = b.c1.x; i < b.c2.x; i++) {
            for (int j = b.c1.y; j < b.c2.y; j++) {
                Coords c = new Coords(i, j);
                if (door != checkedWalls) {
                    if (b.isWall(c)) {
                        checkedWalls++;
                    }
                    continue;
                }
                if (validateDoorLocation(c, b)) {
                    engine.worldIds[i][j] = unlockedDoor;
                    r.doorCoords = c;
                    return r;
                }
                return addDoor(r1, r2);
            }
        }
        return addDoor(r1, r2);
    }

    private boolean validateDoorLocation(Coords c, Bounds b) {
        boolean openTile = false;
        int id1 = engine.worldIds[c.x][c.y];
        if (id1 != unlockedDoor && id1 != lockedDoor && !b.getCorners().contains(c)) {
            for (int x = c.x - 1; x <= c.x + 1; x++) {
                for (int y = c.y - 1; y <= c.y + 1; y++) {
                    if ((x == c.x) == (y == c.y)) {
                        continue;
                    }
                    int id2 = engine.worldIds[x][y];
                    if (id2 == grass || id2 == flower) {
                        openTile = true;
                    } else if (id2 == unlockedDoor || id2 == lockedDoor) {
                        return false;
                    }
                }
            }
        }
        return openTile;
    }

    private void addPortal() {
        while (true) {
            int x = engine.random.nextInt(Engine.WIDTH);
            int y = engine.random.nextInt(Engine.HEIGHT);
            int id = engine.worldIds[x][y];
            if (id != wall && id != lockedDoor && id != unlockedDoor) {
                Coords playerCoords = new Coords(playerX, playerY);
                Coords portalCoords = new Coords(x, y);
                if (getPath(playerCoords, portalCoords) != null) {
                    engine.worldIds[x][y] = portal;
                    return;
                }
            }
        }
    }

    /**
     * Validates a move, and moves the player if valid.
     * @param c  The movement character input by the user.
     */
    void move(char c) {
        Coords north = new Coords(playerX, playerY + 1);
        Coords west = new Coords(playerX - 1, playerY);
        Coords south = new Coords(playerX, playerY - 1);
        Coords east = new Coords(playerX + 1, playerY);
        if (c == 'W') {
            moveTo(north);
        } else if (c == 'A') {
            moveTo(west);
        } else if (c == 'S') {
            moveTo(south);
        } else if (c == 'D') {
            moveTo(east);
        }
    }

    /**
     * Moves the character to the x and y coordinate provided.
     */
    private void moveTo(Coords c) {
        int id = engine.worldIds[c.x][c.y];
        if (walkableTileIds().contains(id)) {
            engine.worldIds[playerX][playerY] = playerOn;
            playerOn = id;
            engine.worldIds[c.x][c.y] = player;
            playerX = c.x;
            playerY = c.y;
        }
    }

    void interact() {
        if (playerOn == portal) {
            generateWorld();
            engine.level++;
        }
    }
}
