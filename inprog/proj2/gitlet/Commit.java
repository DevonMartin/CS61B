package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class

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
    private String parent;

    public Commit(String msg, String parent) {
        new Commit(msg, new Date(), parent);
    }
    private Commit(String msg, Date time, String parent) {
        this.message = msg;
        this.time = time;
        this.parent = parent;
    }
    public static String firstCommit() {
        Commit c = new Commit("initial commit", new Date(0), null);
        return saveCommitment(c);
    }

    public static String saveCommitment(Commit commit) {
        File tmpFile = new File("tmp_commitment");
        Utils.writeObject(tmpFile, commit);
        byte[] b = Utils.readContents(tmpFile);
        String sha = Utils.sha1(b);
        String fileDirectory = Repository.OBJECTS_DIR + "/" + sha.substring(0, 2);
        File permFile = new File(fileDirectory + "/" + sha.substring(2, Utils.UID_LENGTH));
        Utils.writeObject(permFile, commit);
        Utils.restrictedDelete(tmpFile);
        return sha;
    }

    public void time() {
        System.out.println(time);
    }

    /* TODO: fill in the rest of this class. */
}
