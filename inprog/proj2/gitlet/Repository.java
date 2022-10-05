package gitlet;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Devon Martin
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    private static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    private static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The branches, objects and staging directories within .gitlet. */
    public static final File BRANCHES_DIR = join(GITLET_DIR, "branches");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    private static final File STAGING_DIR = join(GITLET_DIR, "staging");
    /** The possible characters in a hexadecimal string. */
    private static String[] HEXADECIMAL_CHARS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    /** The initial commit for every new repository. */
    private String Head = sha1(Commit.firstCommit());
    /** The linked list of commits, starting with */

    private static boolean inRepo() {
        Path path = Paths.get(GITLET_DIR.toURI());
        return Files.exists(path);
    }

    public static void initialize() {
        BRANCHES_DIR.mkdirs();
        OBJECTS_DIR.mkdir();
        for (String c1 : HEXADECIMAL_CHARS) {
            for (String c2 : HEXADECIMAL_CHARS) {
                join(OBJECTS_DIR, c1 + c2).mkdir();
            }
        }
        STAGING_DIR.mkdir();
    }

    /* TODO: fill in the rest of this class. */
}
