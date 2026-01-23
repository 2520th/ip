import java.io.PrintWriter;

public class Monika {

    private static final PrintWriter pw = new PrintWriter(System.out, true);

    private static void addLines(StringBuilder sb) {
        sb.append("____________________________________________________________\n");
    }

    private static void greeting() {
        StringBuilder result = new StringBuilder();
        addLines(result);
        result.append(" Hello! I'm Monika.\n What can I do for you?\n");
        addLines(result);
        pw.println(result);
    }

    private static void exit() {
        StringBuilder result = new StringBuilder();
        addLines(result);
        result.append(" Bye. Hope to see you again soon!\n");
        addLines(result);
        pw.println(result);
        pw.close();
        System.exit(0);
    }

    public static void main(String[] args) {
        greeting();
        exit();
    }
}
