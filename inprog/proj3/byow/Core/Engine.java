package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private TETile[][] world = new TETile[WIDTH][HEIGHT];
    Random random = new Random();
    long seed = random.nextLong();
    boolean TESTING;
    String DATA_DIR = System.getProperty("user.dir") + "/.data";
    TETile white = new TETile(' ', Color.black, Color.white, "");

    /**
     * Creates a new Engine in non-testing mode. Creates required
     * directories if they do not exist.
     */
    Engine() {
        createDataDir();
    }


    /**
     * Creates a new Engine which may be for testing or not. Creates required
     * directories if they do not exist.
     */
    Engine(boolean testing) {
        this.TESTING = testing;
        createDataDir();
    }

    /**
     * Checks for and creates if necessary the required directories.
     */
    private void createDataDir() {
        File dir = Paths.get(DATA_DIR).toFile();
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * Constantly checks for input to StdDraw. Returns the input
     * when detected.
     * @return The character pressed on a keyboard by the user.
     */
    private char listenForCharPress() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (TESTING) {
                    System.out.print(c);
                }
                return c;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        displayMainMenu();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }

    private void generateWorld() {
        generateBlankWorld();
    }

    private void generateWorld(Random r) {
        random = r;
        generateBlankWorld();
    }

    /**
     * Fills the world with 24/25 grass tiles and 1/25 flower tiles.
     */
    private void generateBlankWorld() {
        random.setSeed(seed);
        for (int w = 0; w < WIDTH; w++) {
            for (int h = 0; h < HEIGHT; h++) {
                if (random.nextInt(25) == 0) {
                    world[w][h] = Tileset.FLOWER;
                } else {
                    world[w][h] = Tileset.GRASS;
                }
            }
        }
    }

    /**
     * Clears board to white where Main Menu options will be displayed.
     */
    private void generateMainMenu() {
        generateBlankWorld();
        for (int w = WIDTH / 2 - 8; w < WIDTH / 2 + 8; w++) {
            for (int h = HEIGHT / 2 + 9; h < HEIGHT / 2 + 12; h++) {
                world[w][h] = white;
            }
        }
        for (int w = WIDTH / 2 - 3; w < WIDTH / 2 + 3; w++) {
            for (int h = HEIGHT / 2 + 2; h < HEIGHT / 2 + 6; h++) {
                world[w][h] = white;
            }
        }
    }

    /**
     * Displays the blank world and adds appropriate menus. Listens
     * for input.
     */
    private void displayMainMenu() {
        generateMainMenu();
        displayWorld();
        Font titleFont = new Font("Monaco", Font.BOLD, 32);
        Font generalFont = StdDraw.getFont();
        StdDraw.setFont(titleFont);
        StdDraw.text(WIDTH / 2.0, 25.4, "Room Escape");
        StdDraw.setFont(generalFont);
        StdDraw.text(WIDTH / 2.0, 20, "(N)ew Game");
        StdDraw.text(WIDTH / 2.0, 19, "(L)oad Game");
        StdDraw.text(WIDTH / 2.0, 18, "(Q)uit Game");
        StdDraw.show();
        listenMainMenu();
    }

    /**
     * Listens for input on the Main Menu by the user and parses it
     * appropriately.
     */
    private void listenMainMenu() {
        while (true) {
            char c = listenForCharPress();
            if (c == 'N') {
                getSeed();
                displayMainMenu();
            } else if (c == 'L') {
                loadGame();
            } else if (c == 'Q') {
                System.exit(0);
            }
        }
    }

    /**
     * Updates Main Menu to request a seed input from the User.
     * If a seed is provided, updates class variable random to
     * be a new Random instance with the provided seed. Else,
     * random remains unchanged.
     */
    private void getSeed() {
        seedBox();
        StdDraw.text(WIDTH / 2.0, 15, "Enter seed or leave blank for random.");
        StdDraw.text(WIDTH / 2.0, 14, "Press S to continue.");
        StdDraw.show();
        char c = listenForCharPress();
        String seed = "";
        while (c != 'S') {
            if (Character.isDigit(c) || c == 8) {
                if (c == 8 && seed.length() > 0) {
                    seed = seed.substring(0, seed.length() - 1);
                } else if (seed.length() < 15) {
                    seed += c;
                }
                clearSeedLine(seed);
                StdDraw.text(WIDTH / 2.0, 12, seed);
                StdDraw.show();
            }
            c = listenForCharPress();
        }
        if (seed.equals("")) {
            return;
        }
        this.seed = Long.parseLong(seed);
    }

    private void seedBox() {
        int w = 20;
        for (int y = 10; y < 16; y++) {
            for (int i = 0; i < w; i++) {
                int x = (int) (WIDTH / 2.0 - w / 2 + i);
                if (world[x][y] == white) {
                    continue;
                }
                world[x][y] = white;
                world[x][y].draw(x, y);
            }
        }
        StdDraw.show();
    }

    private void clearSeedLine(String s) {
        int w = Math.max(s.length(), 20);
        for (int i = 0; i < w; i++) {
            int x = (int) (WIDTH / 2.0 - w / 2 + i);
            world[x][11].draw(x, 11);
            world[x][12].draw(x, 12);
        }
    }

    private void loadGame() {
        File f = Paths.get(DATA_DIR, "/save").toFile();
        if (f.exists()) {
            if (TESTING) {
                System.out.println();
            }
            System.out.println("TODO: Need to enable loading game from save.");
        } else {
            System.exit(1);
        }
    }

    private void displayWorld() {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
    }
}
