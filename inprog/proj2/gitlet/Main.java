package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Devon Martin
 */
public class Main {

    static Repository repo;

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        String firstArg = args[0];
        if (firstArg.equals("init")) {
            if (args.length != 1) {
                System.out.println("Incorrect operands.");
                return;
            }
            new Repository();
            return;
        }
        if (!Repository.inRepo()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        repo = Repository.loadRepo();
        switch(firstArg) {
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            case "log":
                repo.log();
                break;
            // TODO: FILL THE REST IN
            default:
                System.out.println("No command with that name exists.");
        }
    }
}
