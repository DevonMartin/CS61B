package gitlet;

// TODO: any imports you need here

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

    public Commit(String msg) {
        this.message = msg;
        this.time = new Date();
    }

    public static Commit firstCommit() {
        Commit c = new Commit("initial commit");
        c.time = new Date(0);
        return c;
    }

    public void time() {
        System.out.println(time);
    }

    /* TODO: fill in the rest of this class. */
}


// long unixTime = Instant.now().getEpochSecond();
// 1577094336