package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.LinkedList;

import static gitlet.Utils.*;


/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Devon Martin
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The time the commit was made. */
    private Date time;
    private String parent1;
    private String parent2;
    private LinkedList<String> addedFiles = new LinkedList<>();
    private String sha;

    public Commit(String msg, String parent1, String parent2) {
        new Commit(msg, new Date(), parent1, parent2);
    }
    private Commit(String msg, Date time, String parent1, String parent2) {
        this.message = msg;
        this.time = time;
        this.parent1 = parent1;
        this.parent2 = parent2;
    }
    public static String firstCommit() {
        Commit c = new Commit("initial commit", new Date(0), null, null);
        c.saveCommitment();
        return c.sha;
    }

    public void saveCommitment() {
        File tmpFile = new File("tmp_commitment");
        writeObject(tmpFile, this);
        byte[] b = Utils.readContents(tmpFile);
        restrictedDelete(tmpFile);
        sha = Utils.sha1(b);
        String fileDirectory = Repository.OBJECTS_DIR + "/" + sha.substring(0, 2);
        File permFile = new File(fileDirectory + "/" + sha.substring(2, UID_LENGTH));
        writeObject(permFile, this);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("===\ncommit " + sha + "\n");
        if (parent2 != null) {
            str.append("Merge: " + parent1.substring(0, 7) + " " + parent2.substring(0, 7) + "\n");
        }
        str.append("Date: " + time + "\n" + message + "\n");
        return str.toString();
    }

    /* TODO: fill in the rest of this class. */
}
