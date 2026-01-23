import java.io.*;
import java.nio.file.*;
import java.util.*;

// IP Week 2 Level 2
// Chat with Monika from Doki Doki Literature Club
public class Monika {

    private static final PrintWriter pw = new PrintWriter(System.out, true);
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static final String line = "____________________________________________________________\n";
    private static String userName;

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
            throw new IOException("Oops, did you just delete my greetings file?");
        }
        String msg = "Hello! I'm Monika. What is your name?";
        if (lines.getFirst().equals("Monika")) {
            say(msg);
            String name = br.readLine();
            while (name.isEmpty() || name.equalsIgnoreCase("monika")) {
                say("Oh no! Please try another name!");
                name = br.readLine();
            }
            say(String.format("Nice to meet you %s, welcome to the Literature Club! What can I help you today?", name));
            lines.set(0, name);
            Files.write(f, lines);
            userName = name;
        } else {
            msg = lines.get(1 + new Random().nextInt(lines.size() - 1));
            userName = lines.getFirst();
            say(msg.replace("[player]", userName));
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
        TaskMgr tasks = new TaskMgr(16);
        // This version will store every non "bye" or "list" command
        while (!(input = br.readLine().toLowerCase()).equals("bye")) {
            String[] tokens = input.trim().split(" ");
            // Identify special commands with length first
            switch (tokens.length) {
                case 1: {
                    switch (tokens[0]) {
                        case "": {
                            say("Say something to me.");
                            break;
                        }
                        case "bye": {
                            exit();
                        }
                        case "list": {
                            say(tasks.list());
                            break;
                        }
                        default: {
                            tasks.addTask(new Task(input));
                            say("added: " + input);
                        }
                    }
                    break;
                }
                default: {
                    tasks.addTask(new Task(input));
                    say("added: " + input);
                }
            }
        }
        exit();
    }
}
