package gitlet;

import java.io.File;
import java.io.Serializable;
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

    /** The message of this Commit. */
    private String message;
    /** The time the commit was made. */
    private String time;
    private String parent1;
    private String parent2;
    private ArrayList<String> files = new ArrayList<>();
    private String sha;
    private final static String blobStr = "x";

    Commit(String msg, String parent1, String parent2) {
        new Commit(msg, new Date(), parent1, parent2);
    }
    private Commit(String msg, Date date, String parent1, String parent2) {
        this.message = msg;
        String pattern = "EEE MMM dd HH:mm:ss yyyy Z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        this.time = simpleDateFormat.format(date);
        this.parent1 = parent1;
        this.parent2 = parent2;
    }
    static Boolean isCommit(String fileName) {
        return fileName.length() == UID_LENGTH - 2;
    }
    static Boolean containsFile(Commit commit, String file) {
        byte[] b = serialize(new File(file));
        String sha = sha1(b) + blobStr;
        return commit.files.contains(sha);
    }
    static String firstCommit() {
        Commit c = new Commit("initial commit", new Date(0), null, null);
        c.saveCommitment();
        return c.sha;
    }

//    static String makeCommitment() {
//
//    }

    public void saveCommitment() {
        byte[] b = serialize(this);
        sha = sha1(b);
        String fileDirectory = Repository.OBJECTS_DIR + "/" + sha.substring(0, 2);
        File file = new File(fileDirectory + "/" + sha.substring(2, UID_LENGTH));
        writeObject(file, this);
    }

    static Commit getCommitFromSha(String sha) {
        String fileDirectory = Repository.OBJECTS_DIR + "/" + sha.substring(0, 2);
        File file = new File(fileDirectory + "/" + sha.substring(2, UID_LENGTH));
        return readObject(file, Commit.class);
    }
    String sha() {
        return sha;
    }
    String message() {
        return message;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("===\ncommit " + sha() + "\n");
        if (parent2 != null) {
            str.append("Merge: ");
            str.append(parent1, 0, 7);
            str.append(" ");
            str.append(parent2, 0, 7);
            str.append("\n");
        }
        str.append("Date: ");
        str.append(time);
        str.append("\n");
        str.append(message());
        str.append("\n");
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
