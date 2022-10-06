package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.*;
import java.util.LinkedList;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Devon Martin
 */
public class Repository implements Serializable {
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
    /** The head, objects, refs and staging directories within .gitlet. */
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    public static final File REFS_DIR = join(GITLET_DIR, "refs");
    private static final File STAGING_DIR = join(GITLET_DIR, "staging");
    /** The possible characters in a hexadecimal string. */
    private static String[] HEXADECIMAL_CHARS =
            { "0", "1", "2", "3", "4", "5", "6", "7",
                    "8", "9", "a", "b", "c", "d", "e", "f" };
    private String branch;
    /** The linked list of commits, starting with the oldest. */
    private LinkedList<String> Commits = new LinkedList<>();

    public Repository() throws IOException {
        if (inRepo()) {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
            return;
        }
        createDirectories();
        addCommit(Commit.firstCommit());
        createBranch("master");
        setHeadToThis();
    }

    private static boolean inRepo() {
        Path path = Paths.get(GITLET_DIR.toURI());
        return Files.exists(path);
    }

    private static void createDirectories() {
        OBJECTS_DIR.mkdirs();
        for (String c1 : HEXADECIMAL_CHARS) {
            for (String c2 : HEXADECIMAL_CHARS) {
                join(OBJECTS_DIR, c1 + c2).mkdir();
            }
        }
        REFS_DIR.mkdir();
        STAGING_DIR.mkdir();
    }
    private void addCommit(String commit) {
        Commits.add(commit);
    }
    private void createBranch(String name) {
        this.branch = name;
        File branchFile = join(REFS_DIR, this.branch);
        writeObject(branchFile, this);
    }
    private void setHeadToThis() throws IOException {
        Path thisBranch = Paths.get(join(REFS_DIR, branch).toURI());
        Path headFile = Paths.get(HEAD.toURI());
        Files.copy(thisBranch, headFile, StandardCopyOption.REPLACE_EXISTING);
    }

    /* TODO: fill in the rest of this class. */
}
