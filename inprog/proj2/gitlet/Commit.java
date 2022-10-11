package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static gitlet.Utils.*;


/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Devon Martin
 */
class Commit implements Serializable {

    private static final int COMMIT_NAME_LENGTH = UID_LENGTH - 2;
    /** The message of this Commit. */
    private String message;
    /** The time the commit was made. */
    private String time;
    private String parent1;
    private String parent2;
    private ArrayList<String> files = new ArrayList<>();
    private String sha;

    private Commit(String msg, Date date, String parent1, String parent2) {
        this.message = msg;
        String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        this.time = simpleDateFormat.format(date);
        this.parent1 = parent1;
        this.parent2 = parent2;
    }
    private static Commit getCommit(String msg, String parent) {
        return new Commit(msg, new Date(), parent, null);
    }
    private Commit getCommit(String msg, String parent1, String parent2) {
        return new Commit(msg, new Date(), parent1, parent2);
    }
    static Commit getCommitFromSha(String sha) {
        String fileDirectory = Repository.OBJECTS_DIR + "/" + sha.substring(0, 2);
        File file = new File(fileDirectory + "/" + sha.substring(2));
        return readObject(file, Commit.class);
    }
    String sha() {
        return sha;
    }
    String message() {
        return message;
    }
    static Boolean isCommit(String fileName) {
        return fileName.length() == COMMIT_NAME_LENGTH;
    }
    static Boolean containsFile(Commit commit, String file) {
        for (String trackedFile : commit.files) {
            trackedFile = trackedFile.substring(COMMIT_NAME_LENGTH - 1);
            if (trackedFile.equals(file)) {
                return true;
            }
        }
        return false;
    }
    static String firstCommit() {
        Commit c = new Commit("initial commit", new Date(0), null, null);
        c.saveCommitment();
        return c.sha();
    }
    public void saveCommitment() {
        byte[] b = serialize(this);
        sha = sha1(b);
        String fileDirectory = Repository.OBJECTS_DIR + "/" + sha.substring(0, 2);
        File file = new File(fileDirectory + "/" + sha.substring(2));
        writeObject(file, this);
    }
    /**
     * Creates a new commit.
     * @param repo The repository gaining a new commitment.
     * @param msg  The message the commitment will store.
     * @return     The sha of the commitment for storage in the repo.
     */
    static String makeCommitment(Repository repo, String msg) {
        String parentString = Repository.latestCommit(repo);
        Commit child = getCommit(msg, parentString);
        Commit parent = getCommitFromSha(parentString);
        /* Children start with the same files as their parents. */
        child.files = (ArrayList<String>) parent.files.clone();
        for (String file : plainFilenamesIn(Repository.STAGING_DIR)) {
            /** Remove the previous version of a file
             * that has been updated and staged. */
            if (Commit.containsFile(child, file)) {
                child.files.remove(file);
            }
            /* Add the file to the new commit. */
            addFileToCommit(file, child);
        }
        /* Remove files from commit that have been staged for removal. */
        for (String file : repo.rmStaging) {
            Commit.removeFileFromCommit(child, file);
        }
        child.saveCommitment();
        return child.sha;
    }
    private static void addFileToCommit(String file, Commit commit) {
        File f = join(Repository.STAGING_DIR, file);
        byte[] b = serialize(f);
        String sha = sha1(b);
        String fullFileName = sha + file;
        commit.files.add(fullFileName);
        saveFileForCommit(f.toPath(), fullFileName);
    }
    /* Move a file from the staging directory into it's sha-directory. */
    private static void saveFileForCommit(Path file, String fileName) {
        Path destination = join(Repository.OBJECTS_DIR,
                fileName.substring(0, 2), fileName.substring(2)).toPath();
        try {
            Files.move(file, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    /* Remove a file from a commit if it exists, else return. */
    private static void removeFileFromCommit(Commit commit, String fileToRemove) {
        for (String file : commit.files) {
            if (file.substring(COMMIT_NAME_LENGTH - 1).equals(fileToRemove)) {
                commit.files.remove(file);
                return;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("===\ncommit " + sha() + "\n");
        if (parent2 != null) {
            str.append("Merge: ").append(parent1, 0, 7).append(" ");
            str.append(parent2, 0, 7).append("\n");
        }
        str.append("Date: ").append(time).append("\n").append(message()).append("\n");
        return str.toString();
    }

    static void log(Commit commit) {
        System.out.println(commit);
        String p1 = commit.parent1;
        if (p1 != null) {
            Commit.log(getCommitFromSha(p1));
        }
    }

    /* TODO: fill in the rest of this class. */
}
