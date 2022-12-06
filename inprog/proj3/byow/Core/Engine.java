package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Engine implements Serializable {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private TETile[][] world = new TETile[WIDTH][HEIGHT];
    // random is used to generate a seed to later update random.
    Random random = new Random();
    // seed is randomly generated for the Random used by the game.
    long seed = random.nextLong();
    // nextSeed is always the first long retrieved from a new random
    long nextSeed;
    InputString inputString;
    boolean TESTING;
    boolean onMainMenu = true;
    String DATA_DIR = System.getProperty("user.dir") + "/.data";
    TETile menuTile = new TETile(' ', Color.black, Color.white, "");
    TETile grass = Tileset.GRASS;
    TETile tree = Tileset.TREE;
    TETile mountain = Tileset.MOUNTAIN;
    TETile flower = Tileset.FLOWER;
    TETile wall = Tileset.WALL;
    TETile floor = Tileset.FLOOR;
    TETile player = Tileset.AVATAR;
    TETile playerOn;

    private class InputString {
        String s;
        int index = 0;
        InputString(String s) {
            this.s = s;
        }
        boolean hasNext() {
            return index < s.length();
        }
        char next() {
            char returnChar = s.charAt(index);
            returnChar = Character.toUpperCase(returnChar);
            index++;
            return returnChar;
        }
    }

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
        ter.initialize(WIDTH, HEIGHT);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int h = HEIGHT - 1; h >= 0; h--) {
            for (int w = 0; w < WIDTH; w++) {
                sb.append(world[w][h].character());
            }
            sb.append("\n");
        }
        return sb.toString();
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
        ter.initialize(WIDTH, HEIGHT);
        while (onMainMenu) {
            displayMainMenu();
        }
        generateWorld();
        displayWorld();
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

        generateBlankWorld();
        generateMainMenu();
        this.inputString = new InputString(input);
        while (inputString.hasNext()) {
            if (onMainMenu) {
                handleMmInput(inputString.next());
                if (!onMainMenu) {
                    generateWorld();
                }
            } else {
                handleGameplayInput(inputString.next());
            }
        }
        return world;
    }

    private void handleMmInput(char c) {
        if (c == 'N') {
            seedBox();
            String seed = "";
            while (c != 'S' && inputString.hasNext()) {
                c = inputString.next();
                seed = updateSeed(seed, c);
            }
            finalizeSeed(seed);
            onMainMenu = false;
        } else if (c == 'L') {
            loadGame();
            onMainMenu = false;
        } else if (c == 'Q') {
            System.exit(0);
        }
    }

    private void handleGameplayInput(char c) {
    }

    private void generateWorld() {
        generateBlankWorld();
        // TODO: Random elements - the actual content
        generateSpawnRoom();
    }

    /**
     * Fills the world with grass, flowers, trees and mountains randomly.
     */
    private void generateBlankWorld() {
        random.setSeed(seed);
        this.nextSeed = random.nextLong();
        TETile last = grass;
        for (int w = 0; w < WIDTH; w++) {
            for (int h = 0; h < HEIGHT; h++) {
                int rInt = random.nextInt(100);
                if (rInt == 0) {
                    world[w][h] = flower;
                } else if (rInt == 1) {
                    world[w][h] = tree;
                } else if (rInt == 2) {
                    world[w][h] = mountain;
                } else {
                    world[w][h] = grass;
                }
            }
        }
    }

    /**
     * Clears board to white where Main Menu options will be displayed.
     */
    private void generateMainMenu() {
        for (int w = WIDTH / 2 - 8; w < WIDTH / 2 + 8; w++) {
            for (int h = HEIGHT / 2 + 9; h < HEIGHT / 2 + 12; h++) {
                world[w][h] = menuTile;
            }
        }
        for (int w = WIDTH / 2 - 3; w < WIDTH / 2 + 3; w++) {
            for (int h = HEIGHT / 2 + 2; h < HEIGHT / 2 + 6; h++) {
                world[w][h] = menuTile;
            }
        }
    }

    /**
     * Displays the blank world and adds appropriate menus. Listens
     * for input.
     */
    private void displayMainMenu() {
        generateBlankWorld();
        generateMainMenu();
        displayWorld();
        addMainMenuText();
        StdDraw.show();
        listenMainMenu();
    }

    private void addMainMenuText() {
        Font titleFont = new Font("Monaco", Font.BOLD, 32);
        Font generalFont = StdDraw.getFont();
        StdDraw.setFont(titleFont);
        StdDraw.text(WIDTH / 2.0, 25.4, "Room Escape");
        StdDraw.setFont(generalFont);
        StdDraw.text(WIDTH / 2.0, 20, "(N)ew Game");
        StdDraw.text(WIDTH / 2.0, 19, "(L)oad Game");
        StdDraw.text(WIDTH / 2.0, 18, "(Q)uit Game");
    }

    /**
     * Listens for input on the Main Menu by the user and parses it
     * appropriately.
     */
    private void listenMainMenu() {
        while (true) {
            char c = listenForCharPress();
            if (c == 'N') {
                String seed = "";
                while (c != 'S') {
                    displayGetSeed();
                    displaySeed(seed);
                    c = listenForCharPress();
                    seed = updateSeed(seed, c);
                }
                finalizeSeed(seed);
                break;
            } else if (c == 'L') {
                loadGame();
                break;
            } else if (c == 'Q') {
                System.exit(0);
            }
        }
        onMainMenu = false;
    }

    private void displayGetSeed() {
        seedBox();
        displayWorld();
        addMainMenuText();
        StdDraw.text(WIDTH / 2.0, 15, "Enter seed or leave blank for random.");
        StdDraw.text(WIDTH / 2.0, 14, "Press S to continue.");
        clearSeedLine();
        StdDraw.show();
    }

    /**
     * Whites out necessary tiles for displaying seed prompt.
     */
    private void seedBox() {
        int w = 20;
        for (int y = 10; y < 16; y++) {
            for (int i = 0; i < w; i++) {
                int x = (int) (WIDTH / 2.0 - w / 2 + i);
                world[x][y] = menuTile;
            }
        }
    }

    /**
     * Updates Main Menu to request a seed input from the User.
     * If a seed is provided, updates class variable random to
     * be a new Random instance with the provided seed. Else,
     * random remains unchanged.
     */
    private String updateSeed(String seed, char c) {
        if (c == 8 && seed.length() > 0) {
            seed = seed.substring(0, seed.length() - 1);
        } else if (Character.isDigit(c) && seed.length() < 30) {
            seed += c;
        }
        return seed;
    }

    private void displaySeed(String seed) {
        StdDraw.text(WIDTH / 2.0, 12, seed);
        StdDraw.show();
    }

    private void clearSeedLine() {
        int w = 20;
        for (int i = 0; i < w; i++) {
            int x = (int) (WIDTH / 2.0 - w / 2 + i);
            world[x][11] = menuTile;
            world[x][12] = menuTile;
        }
    }

    private void finalizeSeed(String seed) {
        if (seed.equals("")) {
            return;
        }
        BigInteger bigInteger = new BigInteger(seed);
        long longSeed = bigInteger.longValue();
        this.seed = longSeed;
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
        ter.renderFrame(world);
    }

    private void generateSpawnRoom() {
        int w = random.nextInt(6) + 6;
        int h = random.nextInt(4) + 6;
        int x = random.nextInt(WIDTH - w);
        int y = random.nextInt(HEIGHT - h);
        for (int i = x; i < x + w; i++) {
            for (int j = y; j < y + h; j++) {
                if (i == x || i == x + w - 1 || j == y || j == y + h - 1) {
                    world[i][j] = wall;
                } else {
                    world[i][j] = floor;
                }
            }
        }
        int playerX = random.nextInt(w - 2) + x + 1;
        int playerY = random.nextInt(h - 2) + y + 1;
        playerOn = world[playerX][playerY];
        world[playerX][playerY] = player;
    }

    private void generateRoom() {

    }
}
