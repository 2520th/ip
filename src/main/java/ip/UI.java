package ip;

import java.io.*;

public class UI {
    private final PrintWriter pw = new PrintWriter(System.out, true);
    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    // UI for saying a string message
    public void say(String msg) {
        final String LINE = "____________________________________________________________\n";
        pw.println(String.format("%s%s\n%s", LINE, msg, LINE));
    }

    // UI for user input
    public String input() throws IOException {
        return br.readLine();
    }
}

