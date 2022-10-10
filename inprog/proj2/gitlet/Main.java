package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author Devon Martin
 */
public class Main {
    /** Helper function to ensure that the number of arguments
     *  is correct for each command. Errors and exits program
     *  if not.
     * @param args The arguments array passed into main
     * @param n    The number of required arguments
     * @return     Whether the number of arguments is correct.
     */
    private static boolean paramLenIsCorrect(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            return false;
        }
        return true;
    }

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        if (firstArg.equals("init")) {
            if (paramLenIsCorrect(args, 1)) {
                new Repository();
                return;
            }
        }
        if (!RepoUtil.inRepo()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        Repository repo = RepoUtil.loadRepo();
        switch (firstArg) {
            case "add":
                if (paramLenIsCorrect(args, 2)) {
                    repo.add(args[1]);
                }
                break;
            case "commit":
                if (paramLenIsCorrect(args, 2)) {

                }
                // TODO: handle the `commit [filename]` command
                break;
            case "rm":
                if (paramLenIsCorrect(args, 2)) {

                }
                // TODO: handle the `rm [filename]` command
                break;
            case "log":
                if (paramLenIsCorrect(args, 1)) {
                    repo.log();
                }
                break;
            case "global-log":
                if (paramLenIsCorrect(args, 1)) {
                    repo.globalLog();
                }
                break;
            case "find":
                if (paramLenIsCorrect(args, 2)) {
                    repo.find(args[1]);
                }
                break;
            case "status":
                if (paramLenIsCorrect(args, 1)) {
                    repo.status();
                }
                break;
            case "checkout":
                // TODO: handle the `checkout [filename]` command
                break;
            case "branch":
                if (paramLenIsCorrect(args, 2)) {
                    repo.branch(args[1]);
                }
                break;
            case "rm-branch":
                if (paramLenIsCorrect(args, 2)) {
                    repo.rmBranch(args[1]);
                }
                break;
            case "reset":
                if (paramLenIsCorrect(args, 2)) {

                }
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
