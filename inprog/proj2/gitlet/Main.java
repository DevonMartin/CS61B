package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Devon Martin
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {

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
        Repository.loadRepo();
        switch (firstArg) {
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            case "commit":
                // TODO: handle the `commit [filename]` command
                break;
            case "rm":
                // TODO: handle the `rm [filename]` command
                break;
            case "log":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.repo.log();
                break;
            case "global-log":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                // TODO: handle the `global-log [filename]` command
                break;
            case "find":
                // TODO: handle the `find [filename]` command
                break;
            case "status":
                if (args.length != 1) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.repo.status();
                break;
            case "checkout":
                // TODO: handle the `checkout [filename]` command
                break;
            case "branch":
                if (args.length != 2) {
                    System.out.println("Incorrect operands.");
                    return;
                }
                Repository.repo.createBranch(args[1]);
                break;
            case "rm-branch":
                // TODO: handle the `rm-branch [filename]` command
                break;
            case "reset":
                // TODO: handle the `reset [filename]` command
                break;
            case "merge":
                // TODO: handle the `merge [filename]` command
                break;
            default:
                System.out.println("No command with that name exists.");
        }
    }
}
