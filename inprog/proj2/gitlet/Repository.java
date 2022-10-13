package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static gitlet.Utils.*;

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Devon Martin
 */
class Repository implements Serializable {

    /** The current working directory. */
    static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    private static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The directories within .gitlet. */
    private static final File HEAD = join(GITLET_DIR, "HEAD");
    static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    private static final File REFS_DIR = join(GITLET_DIR, "refs");
    static final File STAGING_DIR = join(GITLET_DIR, "staging");
    /** The possible characters in a hexadecimal string. */
    private static final String[] HEXADECIMAL_CHARS = { "0", "1", "2", "3", "4", "5", "6", "7",
                                                        "8", "9", "a", "b", "c", "d", "e", "f" };
    /** The branch this repo belongs to. */
    private String branch = "master";
    /** The linked list of commits, starting with the oldest. */
    private String latestCommit;
    /* Return the latest commit of a repository. */
    Commit getLatestCommit() {
        return Commit.getCommitFromSha(latestCommit);
    }
    /** The files staged for removal. */
    ArrayList<String> rmStage = new ArrayList<>();

    /** Initializes a new repository and calls helper functions to:
     *  -create .gitlet and related directories
     *  -create and save an initial commit
     *  -create an initial branch, saving this repo as "master"
     *  -set the current HEAD to this repo
     */
    Repository() {
        if (inRepo()) {
            System.out.println("A Gitlet version-control system"
                    + " already exists in the current directory.");
            return;
        }
        createDirectories();
        latestCommit = Commit.firstCommit();
        branch(branch);
        setHeadToThis(this.branch);
    }

    static boolean inRepo() {
        Path path = GITLET_DIR.toPath();
        return Files.exists(path);
    }

    static Repository loadHead() {
        return readObject(HEAD, Repository.class);
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
    private static void setHeadToThis(String branch) {
        Path thisBranch = join(REFS_DIR, branch).toPath();
        Path headFile = HEAD.toPath();
        try {
            Files.copy(thisBranch, headFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void add(String file) {
        if (plainFilenamesIn(CWD).contains(file)) {
            Commit c = getLatestCommit();
            /** If file has been staged already but has been changed
             * back to the version tracked by the current commit,
             * remove it from the staging area.
             */
            if (plainFilenamesIn(STAGING_DIR).contains(file) && c.containsFileName(file)) {
                join(STAGING_DIR, file).delete();
                return;
            }
            /* If file is tracked and unchanged, do nothing. */
            if (c.containsExactFile(join(CWD, file))) {
                return;
            }
            try {
                Path from = join(CWD, file).toPath(), to = join(STAGING_DIR, file).toPath();
                Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
                if (rmStage.remove(file)) {
                    updateBranch();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        System.out.println("File does not exist.");
    }
    void commit(String msg) {
        if (plainFilenamesIn(STAGING_DIR).size() == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }
        latestCommit = Commit.makeCommitment(this, msg);
        rmStage = new ArrayList<>();
        updateBranch();
    }
    void updateBranch() {
        File branch = join(REFS_DIR, this.branch);
        writeObject(branch, this);
        writeObject(HEAD, this);
    }
    void rm(String file) {
        Boolean check = rmCommit(file);
        if (!(rmStaging(file) || check)) {
            System.out.println("No reason to remove the file.");
        }
    }
    private static Boolean rmStaging(String file) {
        if (plainFilenamesIn(STAGING_DIR).contains(file)) {
            join(STAGING_DIR, file).delete();
            return true;
        }
        return false;
    }
    private Boolean rmCommit(String fileName) {
        Commit c = getLatestCommit();
        if (c.containsFileName(fileName)) {
            rmStage.add(fileName);
            updateBranch();
            File file = join(CWD, fileName);
            if (file.exists()) {
                restrictedDelete(file);
            }
            return true;
        }
        return false;
    }
    void log() {
        getLatestCommit().log();
    }
    static void globalLog() {
        for (String c1 : HEXADECIMAL_CHARS) {
            for (String c2 : HEXADECIMAL_CHARS) {
                File dir = join(OBJECTS_DIR, c1 + c2);
                List<String> files = plainFilenamesIn(dir);
                if (files != null) {
                    for (String file : files) {
                        if (Commit.isCommit(file)) {
                            System.out.println(Commit.getCommitFromSha(c1 + c2 + file));
                        }
                    }
                }
            }
        }
    }
    static void find(String msg) {
        Boolean found = false;
        for (String c1 : HEXADECIMAL_CHARS) {
            for (String c2 : HEXADECIMAL_CHARS) {
                File dir = join(OBJECTS_DIR, c1 + c2);
                List<String> files = plainFilenamesIn(dir);
                if (files != null) {
                    for (String file : files) {
                        if (Commit.isCommit(file)) {
                            File f = join(dir, file);
                            Commit c = readObject(f, Commit.class);
                            if (c.message().equals(msg)) {
                                System.out.println(c.sha());
                                found = true;
                            }
                        }
                    }
                }
            }
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }
    void status() {
        statusBranches();
        statusStagedFiles();
        statusRemovedFiles();
        statusNotStaged();
        statusUntracked();
        System.out.println();
    }
    private void statusBranches() {
        System.out.println("=== Branches ===");
        for (String b : plainFilenamesIn(REFS_DIR)) {
            if (b.equals(branch)) {
                System.out.print("*");
            }
            System.out.println(b);
        }
    }
    private void statusStagedFiles() {
        System.out.println("\n=== Staged Files ===");
        List<String> stagedFiles = plainFilenamesIn(STAGING_DIR);
        if (stagedFiles != null) {
            for (String file : stagedFiles) {
                System.out.println(file);
            }
        }
    }
    private void statusRemovedFiles() {
        System.out.println("\n=== Removed Files ===");
        for (String file : rmStage) {
            System.out.println(file);
        }
    }
    private void statusNotStaged() {
        System.out.println("\n=== Modifications Not Staged For Commit ===");
        Commit c = getLatestCommit();
        for (String fileString : plainFilenamesIn(CWD)) {
            File cwdFile = join(CWD, fileString);
            File stagingFile = join(STAGING_DIR, fileString);
            if (c.containsFileName(fileString)
                    && !c.containsExactFile(cwdFile)
                    && !stagingFile.exists()) {
                System.out.println(fileString + " (modified)");
            } else if (stagingFile.exists()
                    && !Commit.sha1File(cwdFile).equals(Commit.sha1File(stagingFile))) {
                System.out.println(fileString + " (modified)");
            }
        }
        for (String fileString : plainFilenamesIn(STAGING_DIR)) {
            File cwdFile = join(CWD, fileString);
            if (!cwdFile.exists()) {
                System.out.println(fileString + " (deleted)");
            }
        }
        for (String fileString : c.files()) {
            fileString = fileString.substring(UID_LENGTH);
            File cwdFile = join(CWD, fileString);
            File stagingFile = join(STAGING_DIR, fileString);
            if (!rmStage.contains(fileString)
                    && !cwdFile.exists()
                    && !stagingFile.exists()) {
                System.out.println(fileString + " (deleted)");
            }
        }
    }
    private void statusUntracked() {
        System.out.println("\n=== Untracked Files ===");
        Commit c = getLatestCommit();
        for (String file : plainFilenamesIn(CWD)) {
            List<String> stagedFiles = plainFilenamesIn(STAGING_DIR);
            if (!(c.containsFileName(file) || stagedFiles.contains(file))
                || (rmStage.contains(file))) {
                System.out.println(file);
            }
        }
    }
    void checkout(String[] args) {
        Commit c = null;
        String reqFile;
        if (args.length == 3 && args[1].equals("--")) {
            c = getLatestCommit();
            reqFile = args[2];
            checkoutGetFile(c, reqFile);
        } else if (args.length == 4 && args[2].equals("--")) {
            reqFile = args[3];
            String dirString = args[1].substring(0, 2);
            File dir = join(OBJECTS_DIR, dirString);
            String commitStr = args[1].substring(2);
            Boolean found = false;
            for (String file : plainFilenamesIn(dir)) {
                String fileSubstring = file.substring(0, commitStr.length());
                if (Commit.isCommit(file) && fileSubstring.equals(commitStr)) {
                    c = Commit.getCommitFromSha(dirString + file);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("No commit with that id exists.");
            } else {
                checkoutGetFile(c, reqFile);
            }
        } else if (args.length == 2) {

        }
    }
    private void checkoutGetFile(Commit c, String reqFile) {
        if (c.containsFileName(reqFile)) {
            String fullFileName = c.getFile(reqFile);
            String dirName = fullFileName.substring(0, 2);
            String fileName = fullFileName.substring(2);
            File dir = join(OBJECTS_DIR, dirName);
            Path to = join(CWD, reqFile).toPath();
            Path from = join(dir, fileName).toPath();
            try {
                Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File does not exist in that commit.");
        }
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
        if (name.equals(branch)) {
            System.out.println("Cannot remove the current branch.");
        } else if (!join(REFS_DIR, name).delete()) {
            System.out.println("A branch with that name does not exist.");
        }
    }
}
