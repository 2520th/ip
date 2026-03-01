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
        storage.setupDataFolder();
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
        List<String> lines = storage.getLines("/greetings");
        List<String> data = storage.readLines("data");
        String msg = "Hello! I'm Monika. What is your name?";
        String userName;
        if (data.get(0).equals("Monika")) {
            inputState = 1;
        } else {
            msg = lines.get(data.get(1).charAt(0) == '1' ? 20 + new Random().nextInt(7) : new Random().nextInt(20));
            userName = data.get(0);
            msg = msg.replace("[player]", userName);
        }
        data.set(1, "1");
        storage.writeLines("data", data);
        return msg;
    }

    public String getResponse(String input) throws IOException {

        // inputState can only be 0 or 1 for now, it is not a boolean for more potential future interaction dialog updates
        // State 0 (regular dialog)
        if (inputState == 0) {
            Command cmd = parser.parse(input);
            String res = exe.execute(cmd, tasks);
            if (res == null) {
                storeFiles();
                System.exit(0);
            }
            return res;
        }

        // State 1 (asking for user to input username when the application is first launched)
        // Illegal names branch
        if (input.isEmpty() || input.equalsIgnoreCase("monika")) {
            return "Oh no! Please try another name!";
        }

        // Successfully store username
        inputState = 0;
        List<String> lines = storage.readLines("data");
        lines.set(0, input);
        storage.writeLines("data", lines);
        return String.format("Nice to meet you %s, welcome to the Literature Club! What can I help you today?", input);
    }

    private void storeFiles() throws IOException {
        storage.storeTasks(tasks);
        List<String> lines = storage.readLines("data");
        // records exit status
        lines.set(1, "0");
        storage.writeLines("data", lines);
    }
}
