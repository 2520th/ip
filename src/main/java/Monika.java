import java.io.*;
import java.nio.file.*;
import java.util.*;

// IP Week 2 Level 1
// Chat with Monika from Doki Doki Literature Club
public class Monika {

    private static final PrintWriter pw = new PrintWriter(System.out, true);
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final String line = "____________________________________________________________\n";

    private static void say(String msg) {
        pw.println(String.format("%s%s\n%s", line, msg, line));
    }

    // Adapted some randomized greetings from the mod "Monika After Story" from:
    // https://github.com/Monika-After-Story/MonikaModDev/blob/master/Monika%20After%20Story/game/script-greetings.rpy
    private static void greeting() throws IOException {
        Path f = Paths.get("greetings.txt");
        List<String> lines;
        try {
            lines = Files.readAllLines(f);
        } catch (FileNotFoundException e) {
            throw new IOException("Missing files");
        }
        String msg = "Hello! I'm Monika. What is your name?";
        if (lines.getFirst().equals("Monika")) {
            say(msg);
            String name = br.readLine();
            while (name.isEmpty() || name.equals("Monika")) {
                say("Oh no! Please try another name!");
                name = br.readLine();
            }
            say(String.format("Nice to meet you %s, welcome to the Literature Club!", name));
            lines.set(0, name);
            Files.write(f, lines);
        } else {
            msg = lines.get(1 + new Random().nextInt(lines.size() - 1));
            say(msg.replace("[player]", lines.getFirst()));
        }
    }

    private static void exit() {
        say(" Bye. Hope to see you again soon!");
        pw.close();
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        greeting();
        String input;
        // This version will echo until user input is "bye"
        while (!(input = br.readLine()).equalsIgnoreCase("bye")) {
            say(input);
        }
        exit();
    }
}
