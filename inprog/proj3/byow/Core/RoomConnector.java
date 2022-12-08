package byow.Core;

import java.util.*;

import byow.Core.WorldGenerator.*;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class RoomConnector {

    static TETile[][] world = null;

    /**
     * Connects two rooms, either with a hallway or by open space.
     */
    static void connectRooms(WorldGenerator.Room r1, WorldGenerator.Room r2, TETile[][] world, Random r, WorldGenerator wg) {
        RoomConnector.world = world;
        Stack<Coords> path = getPath(r1, r2);
        try {
            while (path == null) {
                r1 = wg.generateRoomAt(r1.b, r);
                path = getPath(r1, r2);
                if (path == null) {
                    r2 = wg.generateRoomAt(r2.b, r);
                    path = getPath(r1, r2);
                }
            }
        } catch (StackOverflowError ignored) {}
        if (path == null) {
            return;
        }
        while (!path.isEmpty()) {
            Coords c = path.pop();
            world[c.x][c.y] = Tileset.FLOOR;
        }
    }


    private static Stack<Coords> getPath(Room r1, Room r2) {
        Node current = new Node(r1.doorCoords);
        Node destination = new Node(r2.doorCoords);
        ArrayList<Node> checkedTiles = new ArrayList<>();
        checkedTiles.add(current);
        Queue<Node> queue = new LinkedList<>();
        addSurroundingTiles(queue, current, checkedTiles);
        return checkTiles(queue, destination, checkedTiles);
    }

    private static void addSurroundingTiles(Queue<Node> q, Node n, ArrayList<Node> marked) {
        for (int i = n.c.x - 1; i <= n.c.x + 1; i++) {
            if (i < 0 || i >= Engine.WIDTH) {
                continue;
            }
            for (int j = n.c.y - 1; j <= n.c.y + 1; j++) {
                if (j < 0 || j >= Engine.HEIGHT) {
                    continue;
                }
                if (!(i == n.c.x || j == n.c.y)) {
                    continue;
                }
                String d = world[i][j].description();
                if (!(WorldGenerator.replaceableTileDescriptions().contains(d))) {
                    continue;
                }
                Coords c = new Coords(i, j);
                Node newNode = new Node(c);
                newNode.parent = n;
                if (!marked.contains(newNode)) {
                    q.add(newNode);
                    marked.add(newNode);
                }
            }
        }
    }
    private static Stack<Coords> checkTiles(Queue<Node> q, Node destination, ArrayList<Node> marked) {
        if (q.isEmpty()) {
            return null;
        }
        Node current = q.remove();
        if (current.equals(destination)) {
            Stack<Coords> s = new Stack<>();
            current = current.parent;
            while (current.parent != null) {
                s.add(current.c);
                current = current.parent;
            }
            return s;
        }
        addSurroundingTiles(q, current, marked);
        return checkTiles(q, destination, marked);
    }
}
