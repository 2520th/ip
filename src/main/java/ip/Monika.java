package ip;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Monika {

    private final Parser parser = new Parser();
    private final Executor exe = new Executor();
    private final Storage storage = new Storage();
    private final TaskMgr tasks;

    // 0 = normal, 1 = waiting for a username input
    private int inputState = 0;

    public Monika() throws IOException {
        tasks = storage.loadTasks();
    }

    /**
     * Greeting method asking for username for the first run
     * Randomly picks a greeting from the mod "Monika After Story":
     * github.com/Monika-After-Story/MonikaModDev/blob/master/Monika%20After%20Story/game/script-greetings.rpy
     * IOException handled due to BufferedReader and PrintWriter from ui
     */
    public String greeting() throws IOException {
        Storage storage = new Storage();
        List<String> lines = storage.readLines("greetings.txt");
        String msg = "Hello! I'm Monika. What is your name?";
        String userName;
        if (lines.get(0).equals("Monika")) {
            inputState = 1;
        } else {
            msg = lines.get(1 + new Random().nextInt(lines.size() - 1));
            userName = lines.get(0);
            msg = msg.replace("[player]", userName);
        }
        return msg;
    }

    public String getResponse(String input) throws IOException {
        // Regular inputs goes here
        if (inputState == 0) {
            Command cmd = parser.parse(input);
            String s = exe.execute(cmd, tasks);
            if (s == null) {
                storage.storeTasks(tasks);
                System.exit(0);
            }
            return s;
        }

        // Illegal names branch
        if (input.isEmpty() || input.equalsIgnoreCase("monika")) {
            return "Oh no! Please try another name!";
        }

        // Successfully store username
        inputState = 0;
        List<String> lines = storage.readLines("greetings.txt");
        lines.set(0, input);
        storage.writeLines("greetings.txt", lines);
        return String.format("Nice to meet you %s, welcome to the Literature Club! What can I help you today?", input);
    }
}
