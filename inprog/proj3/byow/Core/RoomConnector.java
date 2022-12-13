package byow.Core;

import java.util.*;

import byow.Core.WorldGenerator.*;

import static byow.Core.Engine.*;

public class RoomConnector {

    static int[][] world = null;

    /**
     * Connects two rooms, either with a hallway or by open space.
     */
    static void connectRooms(WorldGenerator.Room r1, WorldGenerator.Room r2, int[][] world, Random r, WorldGenerator wg) {
        RoomConnector.world = world;
        Stack<Coords> path = getPath(r1, r2);
        try {
            Coords c;
            while (path == null) {
                c = r1.doorCoords;
                world[c.x][c.y] = wall;
                r1 = wg.generateRoomAt(r1.b, r);
                if (r1 == null) {
                    return;
                }
                path = getPath(r1, r2);
                if (path == null) {
                    c = r2.doorCoords;
                    world[c.x][c.y] = wall;
                    r2 = wg.generateRoomAt(r2.b, r);
                    if (r2 == null) {
                        return;
                    }
                    path = getPath(r1, r2);
                }
            }
        } catch (StackOverflowError ignored) {}
        if (path == null) {
            return;
        }
        boolean indoors = r.nextInt(4) != 0;
        if (!indoors) {
            return;
        }
        ArrayList<Coords> hallway = new ArrayList<>();
        while (!path.isEmpty()) {
            Coords c = path.pop();
            world[c.x][c.y] = floor;
            hallway.add(c);
            wg.usedTiles.add(c);
        }
        addHallwayWalls(hallway, wg);
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
            if (i <= 0 || i >= Engine.WIDTH - 1) {
                continue;
            }
            for (int j = n.c.y - 1; j <= n.c.y + 1; j++) {
                if (j <= 0 || j >= Engine.HEIGHT - 1) {
                    continue;
                }
                if (!(i == n.c.x || j == n.c.y)) {
                    continue;
                }
                int id = world[i][j];
                if (!(WorldGenerator.replaceableTileDescriptions().contains(id))) {
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
        while (!q.isEmpty()) {
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
        }
        return null;
    }
    private static void addHallwayWalls(ArrayList<Coords> floorTiles, WorldGenerator wg) {
        floorTiles.forEach((t) -> {
            for (int i = t.x - 1; i <= t.x + 1; i++) {
                for (int j = t.y - 1; j <= t.y + 1; j++) {
                    int id = world[i][j];
                    if (!(id == floor || id == lockedDoor || id == unlockedDoor)) {
                        Coords c = new Coords(i, j);
                        world[i][j] = wall;
                        wg.usedTiles.add(c);
                    }
                }
            }
        });
    }
}
