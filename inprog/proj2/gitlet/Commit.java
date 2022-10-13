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
    private final String message;
    /** The time the commit was made. */
    private final String time;
    private final String parent1;
    private final String parent2;
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
    private static Commit getCommit(String msg, String parent1, String parent2) {
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
    ArrayList<String> files() {
        return files;
    }
    static Boolean isCommit(String fileName) {
        return fileName.length() == COMMIT_NAME_LENGTH;
    }
    Boolean containsFileName(String file) {
        for (String trackedFile : files) {
            trackedFile = trackedFile.substring(UID_LENGTH);
            if (trackedFile.equals(file)) {
                return true;
            }
        }
        return false;
    }

    /** Takes in a file from the CWD and returns true if
     * the Commit has the same version of the file stored.
     */
    Boolean containsExactFile(File file) {
        String fileName = sha1File(file) + file.getName();
        return files.contains(fileName);
    }
    public String getFile(String reqFile) {
        for (String fileName : files) {
            if (fileName.substring(UID_LENGTH).equals(reqFile)) {
                return fileName;
            }
        }
        return "";
    }
    static String sha1File(File file) {
        String s = readContentsAsString(file);
        return sha1(serialize(s));
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
        Commit parent = repo.getLatestCommit();
        Commit child = getCommit(msg, parent.sha);
        /* Children start with the same files as their parents.
         */
        child.files = (ArrayList<String>) parent.files.clone();
        for (String file : plainFilenamesIn(Repository.STAGING_DIR)) {
            /* Remove the previous version of a file
             * that has been updated and staged.
             */
            if (child.containsFileName(file)) {
                child.removeFileFromCommit(file);
            }
            /* Add the file to the new commit.
             */
            child.addFileToCommit(file);
        }
        /* Remove files from commit that have been staged for removal.
         */
        for (String file : repo.rmStage) {
            child.removeFileFromCommit(file);
        }
        child.saveCommitment();
        return child.sha;
    }
    private void addFileToCommit(String fileString) {
        File file = join(Repository.STAGING_DIR, fileString);
        String fileSha = sha1File(file);
        String fullFileName = fileSha + fileString;
        files.add(fullFileName);
        saveFileForCommit(file.toPath(), fullFileName);
    }
    /* Move a file from the staging directory into it's sha-directory.
     */
    private static void saveFileForCommit(Path file, String fileName) {
        Path destination = join(Repository.OBJECTS_DIR,
                fileName.substring(0, 2), fileName.substring(2)).toPath();
        try {
            Files.move(file, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /* Remove a file from a commit if it exists, else return.
     */
    private void removeFileFromCommit(String fileToRemove) {
        for (String file : files) {
            if (file.substring(UID_LENGTH).equals(fileToRemove)) {
                files.remove(file);
                return;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("===\ncommit " + sha + "\n");
        if (parent2 != null) {
            str.append("Merge: ").append(parent1, 0, 7).append(" ");
            str.append(parent2, 0, 7).append("\n");
        }
        str.append("Date: ").append(time).append("\n").append(message).append("\n");
        return str.toString();
    }

    void log() {
        System.out.println(this);
        if (parent1 != null) {
            getCommitFromSha(parent1).log();
        }
    }
}
