package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Engine implements Serializable {

    private final transient TERenderer ter = new TERenderer();
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    int[][] worldIds = new int[WIDTH][HEIGHT];
    private WorldGenerator wg = new WorldGenerator(this);
    // random is used to generate a seed to later update random.
    Random random = new Random();
    // seed is randomly generated for the Random used by the game.
    private long seed = random.nextLong();
    // nextSeed is always the first long retrieved from a new random
    private long nextSeed;
    private transient InputString inputString;
    private transient boolean onMainMenu = true;
    private static String DATA_DIR = System.getProperty("user.dir") + "/.data";
    private static TETile menuTile = new TETile(' ', Color.black, Color.white, "");
    private Hashtable<Integer, TETile> Tiles = new Hashtable<>();
    static final int grass = 0;
    static final int tree = 1;
    static final int mountain = 2;
    static final int flower = 3;
    static final int wall = 4;
    static final int floor = 5;
    static final int lockedDoor = 6;
    static final int unlockedDoor = 7;
    static final int player = 8;
    static final int portal = 9;
    static final int menu = 10;

    private static class InputString implements InputDevice {
        String s;
        int index = 0;
        InputString(String s) {
            this.s = s;
        }
        public boolean hasNext() {
            return index < s.length();
        }
        public char next() {
            char returnChar = s.charAt(index);
            returnChar = Character.toUpperCase(returnChar);
            index++;
            return returnChar;
        }
    }

    private static class InputKeyboard implements InputDevice {
        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public char next() {
            while (true) {
                if (StdDraw.hasNextKeyTyped()) {
                    char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                    return c;
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Creates a new Engine in non-testing mode. Creates required
     * directories if they do not exist.
     */
    Engine() {
        populateTiles();
        createDataDir();
    }




    /**************************
     *   UTILITY FUNCTIONS   *
    /*************************/

    private void populateTiles() {
        Tiles.put(grass, Tileset.GRASS);
        Tiles.put(tree, Tileset.TREE);
        Tiles.put(mountain, Tileset.MOUNTAIN);
        Tiles.put(flower, Tileset.FLOWER);
        Tiles.put(wall, Tileset.WALL);
        Tiles.put(floor, Tileset.FLOOR);
        Tiles.put(lockedDoor, Tileset.LOCKED_DOOR);
        Tiles.put(unlockedDoor, Tileset.UNLOCKED_DOOR);
        Tiles.put(player, Tileset.AVATAR);
        Tiles.put(portal, Tileset.PORTAL);
        Tiles.put(menu, menuTile);
    }

    @Override
    public String toString() {
        TETile[][] world = makeWorld();
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
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
     * Handles input on the Main Menu.
     * @param id      An InputDevice used for obtaining input to the game.
     * @param display Whether the game is being displayed visually.
     */
    private void listenMainMenu(InputDevice id, boolean display) {
        while (id.hasNext()) {
            char c = id.next();
            if (c == 'N') {
                String seed = "";
                while (id.hasNext() && c != 'S') {
                    if (display) {
                        displayGetSeed();
                        displaySeed(seed);
                    }
                    c = id.next();
                    seed = updateSeed(seed, c);
                }
                finalizeSeed(seed);
                generateWorld();
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

    /**
     * Handles input during Gameplay.
     * @param id      An InputDevice used for obtaining input to the game.
     * @param display Whether the game is being displayed visually.
     */
    private void listenGameplay(InputDevice id, boolean display) {
        while (id.hasNext()) {
            char c = id.next();
            if (c == 'N') {
                seed = random.nextLong();
                generateWorld();
            } else if (c == 'W' || c == 'A' || c == 'S' || c == 'D') {
                wg.move(c);
            } else if (c == ' ') {
                wg.interact();
            } else if (c == ':') {
                if (id.hasNext() && id.next() == 'Q') {
                    save();
                    System.exit(0);
                }
            }
            if (display) {
                displayWorld();
            }
        }
    }

    private void save() {
        try {
            FileOutputStream file = new FileOutputStream(DATA_DIR + "/save.txt");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this);
            out.close();
            file.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    private void loadGame() {
        File f = Paths.get(DATA_DIR, "/save.txt").toFile();
        if (f.exists()) {
            try {
                FileInputStream file = new FileInputStream(f);
                ObjectInputStream obj = new ObjectInputStream(file);
                Engine e = (Engine) obj.readObject();
                obj.close();
                file.close();
                this.nextSeed = e.nextSeed;
                this.random = e.random;
                this.seed = e.seed;
                this.wg = e.wg;
                this.worldIds = e.worldIds;
            } catch (IOException | ClassNotFoundException i) {
                i.printStackTrace();
            }
        } else {
            System.exit(1);
        }
    }

    private TETile[][] makeWorld() {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                world[i][j] = Tiles.get(worldIds[i][j]);
            }
        }
        return world;
    }




    /*******************************
     *   INPUT STRING FUNCTIONS   *
     /*****************************/

    /**
     * Method used for testing code. The input string will be a series of characters
     * (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww"). The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * @param input the input string to feed to your program
     * @return      the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        generateBlankWorld();
        generateMainMenu();
        this.inputString = new InputString(input);
        listenMainMenu(inputString, false);
        if (!onMainMenu) {
            generateWorld();
            listenGameplay(inputString, false);
        }
        return makeWorld();
    }

    /**
     * Obtains a random world from the WorldGenerator. Resets
     * the random with the saved seed before creation.
     */
    private void generateWorld() {
//        System.out.println(seed);
        random.setSeed(seed);
        this.nextSeed = random.nextLong();
        wg.generateWorld();
    }

    /**
     * Populates the game with a background but no content. Used for
     * developing the Main Menu.
     */
    private void generateBlankWorld() {
        random.setSeed(seed);
        this.nextSeed = random.nextLong();
        wg.generateBlankWorld();
    }

    /**
     * Clears board to white where Main Menu options will be displayed.
     */
    private void generateMainMenu() {
        for (int w = WIDTH / 2 - 8; w < WIDTH / 2 + 8; w++) {
            for (int h = HEIGHT / 2 + 9; h < HEIGHT / 2 + 12; h++) {
                worldIds[w][h] = menu;
            }
        }
        for (int w = WIDTH / 2 - 3; w < WIDTH / 2 + 3; w++) {
            for (int h = HEIGHT / 2 + 2; h < HEIGHT / 2 + 6; h++) {
                worldIds[w][h] = menu;
            }
        }
    }

    /**
     * Whites out necessary tiles for displaying seed prompt.
     */
    private void seedBox() {
        int w = 20;
        for (int y = 10; y < 16; y++) {
            for (int i = 0; i < w; i++) {
                int x = (int) (WIDTH / 2.0 - w / 2 + i);
                worldIds[x][y] = menu;
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

    private void finalizeSeed(String seed) {
        if (seed.equals("")) {
            return;
        }
        try {
            BigInteger bigInteger = new BigInteger(seed);
            this.seed = bigInteger.longValue();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }




    /***********************************
     *   KEYBOARD/DISPLAY FUNCTIONS   *
     /*********************************/

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        displayMainMenu();
        displayWorld();
        listenGameplay(new InputKeyboard(), true);
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
        listenMainMenu(new InputKeyboard(), true);
    }

    /**
     * Adds text to main menu.
     */
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
     * Adds text to seed box to prompt user.
     */
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
     * Displays the seed typed by the user in the Seed Box.
     */
    private void displaySeed(String seed) {
        StdDraw.text(WIDTH / 2.0, 12, seed);
        StdDraw.show();
    }

    /**
     * Clears the Seed from the Seed Box in preparation for displaying an updated seed.
     */
    private void clearSeedLine() {
        int w = 20;
        for (int i = 0; i < w; i++) {
            int x = (int) (WIDTH / 2.0 - w / 2 + i);
            worldIds[x][11] = menu;
            worldIds[x][12] = menu;
        }
    }

    /**
     * Renders the world to StdDraw.
     */
    private void displayWorld() {
        TETile[][] world = makeWorld();
        ter.renderFrame(world);
    }
}
