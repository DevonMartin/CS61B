package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Devon Martin
 */
public class Repository implements Serializable {

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
    private static String[] HEXADECIMAL_CHARS = { "0", "1", "2", "3", "4", "5", "6", "7",
                                                  "8", "9", "a", "b", "c", "d", "e", "f" };
    /** The currently loaded repo that you are in. */
    static Repository repo;
    /** The branch this repo belongs to. */
    private String branch;
    /** The linked list of commits, starting with the oldest. */
    private String latestCommit;

    /** Initializes a new repository and calls helper functions to:
     *  -create .gitlet and related directories
     *  -create and save an initial commit
     *  -create an initial branch, saving this repo as "master"
     *  -set the current HEAD to this repo
     */
    Repository() {
        if (inRepo()) {
            String s = "A Gitlet version-control system already exists in the current directory.";
            System.out.println(s);
            return;
        }
        createDirectories();
        latestCommit = Commit.firstCommit();
        this.branch = "master";
        branch(this.branch);
        setHeadToThis();
    }

    static boolean inRepo() {
        Path path = Paths.get(GITLET_DIR.toURI());
        return Files.exists(path);
    }

    static void loadRepo() {
        repo = readObject(HEAD, Repository.class);
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
    private void setHeadToThis() {
        Path thisBranch = Paths.get(join(REFS_DIR, this.branch).toURI());
        Path headFile = Paths.get(HEAD.toURI());
        try {
            Files.copy(thisBranch, headFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void log() {
        Commit.getCommitFromSha(latestCommit).log();
    }
    void globalLog() {
        for (String c1 : HEXADECIMAL_CHARS) {
            for (String c2 : HEXADECIMAL_CHARS) {
                File dir = join(OBJECTS_DIR, c1 + c2);
                List<String> files = plainFilenamesIn(dir);
                if (files != null) {
                    for (String file : files) {
                        file = c1 + c2 + file;
                        if (Commit.isCommit(file)) {
                            System.out.println(Commit.getCommitFromSha(file));
                        }
                    }
                }
            }
        }
    }
    void find(String msg) {

    }
    void status() {
        System.out.println("=== Branches ===");
        for (String b : plainFilenamesIn(REFS_DIR)) {
            if (b.equals(this.branch)) {
                System.out.print("*");
            }
            System.out.println(b);
        }
        System.out.println("\n=== Staged Files ===");
        List<String> stagedFiles = plainFilenamesIn(STAGING_DIR);
        if (stagedFiles != null) {
            for (String file : stagedFiles) {
                System.out.println(file);
            }
        }
        System.out.println("\n=== Removed Files ===");
        System.out.println("\n=== Modifications Not Staged For Commit ===");
        System.out.println("\n=== Untracked Files ===");
        System.out.println();
    }
    void branch(String name) {
        for (String file : plainFilenamesIn(REFS_DIR)) {
            if (file.equals(name)) {
                System.out.println("A branch with that name already exists.");
                return;
            }
        }
        File branchFile = join(REFS_DIR, name);
        writeObject(branchFile, this);
    }
    void rmBranch(String name) {
        if (name.equals(this.branch)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        List<String> branches = plainFilenamesIn(REFS_DIR);
        for (String b : branches) {
            if (b.equals(name)) {
                join(REFS_DIR, b).delete();
                return;
            }
        }
        System.out.println("A branch with that name does not exist.");
    }

    /* TODO: fill in the rest of this class. */
}
