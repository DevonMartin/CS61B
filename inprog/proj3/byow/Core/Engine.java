package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private TETile[][] world = new TETile[WIDTH][HEIGHT];
    Random random = new Random();
    boolean TESTING;

    Engine() {}

    Engine(boolean testing) {
        this.TESTING = testing;
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        displayMainMenu();
//        displayWorld();
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

    private void generateBlankWorld() {
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

    private void generateMainMenu() {
        generateBlankWorld();
        TETile white = new TETile(' ', Color.black, Color.white, "");
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

    private void displayMainMenu() {
        generateMainMenu();
        displayWorld();
        Font titleFont = new Font("Monaco", Font.BOLD, 32);
        Font generalFont = StdDraw.getFont();
        StdDraw.setFont(titleFont);
        StdDraw.text(WIDTH / 2, 25.4, "Room Escape");
        StdDraw.setFont(generalFont);
        StdDraw.text(WIDTH / 2, 20, "(N)ew Game");
        StdDraw.text(WIDTH / 2, 19, "(L)oad Game");
        StdDraw.text(WIDTH / 2, 18, "(Q)uit Game");
        StdDraw.show();
        listenMainMenu();
    }

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

    private void listenMainMenu() {
        while (true) {
            char c = listenForCharPress();
            if (c == 'N') {

            } else if (c == 'L') {

            } else if (c == 'Q') {
                System.exit(0);
            }
        }
    }

    private void getSeed() {

    }

    private void displayWorld() {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(world);
    }
}
