package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.white);
        StdDraw.enableDoubleBuffering();

        this.rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = rand.nextInt(CHARACTERS.length);
            char c = CHARACTERS[index];
            sb.append(c);
        }
        String s = sb.toString();
        return s;
    }

    public void drawFrame(String s) {
        Font headerFont = new Font("Monaco", Font.ROMAN_BASELINE, 12);
        Font gameplayFont = new Font("Monaco", Font.BOLD, 30);

        StdDraw.clear(Color.white);
        StdDraw.rectangle(width / 2, height, width / 2, 1);
        StdDraw.setFont(headerFont);
        StdDraw.textLeft(1, height - 0.5, "Round: " + this.round);
        StdDraw.text(width / 2, height - 0.5, this.playerTurn ? "Type!" : "Watch!");
        int index = rand.nextInt(ENCOURAGEMENT.length);
        StdDraw.textRight(width - 1, height - 0.5, ENCOURAGEMENT[index]);

        StdDraw.setFont(gameplayFont);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            sleep(500);
            String s = letters.substring(i, i + 1);
            drawFrame(s);
            sleep(1000);
            StdDraw.clear(Color.white);
            drawFrame("");
        }
    }

    private void sleep(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public String solicitNCharsInput(int n) {
        String s = "";
        drawFrame(s);
        while (s.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                s += StdDraw.nextKeyTyped();
                drawFrame(s);
            }
        }
        return s;
    }

    public void startGame() {
        this.round = 1;
        while (true) {
            drawFrame("Round: " + this.round);
            sleep(1000);
            StdDraw.clear(Color.white);
            String req = generateRandomString(this.round);
            flashSequence(req);
            this.playerTurn = true;
            String res = solicitNCharsInput(this.round);
            sleep(1000);
            StdDraw.clear(Color.white);
            this.playerTurn = false;
            if (res.equals(req)) {
                this.round++;
            } else {
                drawFrame("Game Over! You made it to round: " + this.round);
                sleep(2500);
                System.exit(0);
            }
        }
    }

}
