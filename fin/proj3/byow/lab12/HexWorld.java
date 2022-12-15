package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

import static java.lang.Integer.parseInt;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private int size;
    private int maxWidthOfHexagon;
    public int width;
    public int height;
    private int nextX;
    private int nextY = 1;
    public TETile[][] hexagons;
    private static final Random RANDOM = new Random();
    HexWorld(int size) {
        this.size = size;
        this.maxWidthOfHexagon = size + (size - 1) * 2;
        this.width = maxWidthOfHexagon * 3 + size * 2 + 2;
        this.height = size * 10 + 2;
        this.nextX = width / 2 - size / 2 + size - 1;
        makeHexagon();
        fillHexagon();
    }

    private void makeHexagon() {
        hexagons = new TETile[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                hexagons[i][j] = Tileset.NOTHING;
            }
        }
    }

    private void fillHexagon() {
        addHexagon(getRandomTile());
        int nextRowX = nextX - ((size - 1) * 2 + size * 2);
        int nextRowY = nextY;
        for (int i = 0; i < 2; i++) {
            addHexagon(getRandomTile());
        }
        nextX = nextRowX;
        nextY = nextRowY;
        addHexagon(getRandomTile());
        nextRowX = nextX - ((size - 1) * 2 + size * 2);
        nextRowY = nextY;
        for (int i = 0; i < 3; i++) {
            addHexagon(getRandomTile());
        }
        nextX = nextRowX;
        nextY = nextRowY;
        for (int i = 0; i < 5; i++) {
            addHexagon(getRandomTile());
        }
        nextX = nextRowX;
        nextY = nextRowY + size * 2;
        for (int i = 0; i < 4; i++) {
            addHexagon(getRandomTile());
        }
        nextX = nextRowX;
        nextY = nextRowY + size * 4;
        for (int i = 0; i < 3; i++) {
            addHexagon(getRandomTile());
        }
    }

    private void addHexagon(TETile t) {
        for (int i = 0; i < size; i++) {
            for (int j = 0 - i; j < size + i; j++) {
                hexagons[nextX - j][nextY + i] = t;
            }
        }
        nextY += size;
        for (int i = 0; i < size; i++) {
            for (int j = ((size - 1) * 2) - i; j > 0 + i - size; j--) {
                hexagons[nextX - j][nextY + i] = t;
            }
        }
        nextX = nextX + (size - 1) * 2 + 1;
    }

    private TETile getRandomTile() {
        int tileNum = RANDOM.nextInt(9);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.FLOOR;
            case 3: return Tileset.GRASS;
            case 4: return Tileset.MOUNTAIN;
            case 5: return Tileset.SAND;
            case 6: return Tileset.WATER;
            case 7: return Tileset.AVATAR;
            default: return Tileset.TREE;
        }
    }

    public static void main(String[] args) {
        HexWorld world = new HexWorld(parseInt(args[0]));
        TERenderer ter = new TERenderer();
        ter.initialize(world.width, world.height);
        ter.renderFrame(world.hexagons);
    }
}