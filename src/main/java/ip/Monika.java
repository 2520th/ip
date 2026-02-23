package ip;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Monika {

    private final Parser parser = new Parser();
    private final Executor exe = new Executor();
    private final Storage storage = new Storage();
    private final TaskMgr tasks;

    public Monika() throws IOException {
        tasks = storage.loadTasks();
    }

    /**
     * Greeting method asking for username for the first run
     * Randomly picks a greeting from the mod "Monika After Story":
     * github.com/Monika-After-Story/MonikaModDev/blob/master/Monika%20After%20Story/game/script-greetings.rpy
     * @param ui
     * @throws IOException
     * IOException handled due to BufferedReader and PrintWriter from ui
     */
    private static void greeting(UI ui) throws IOException {
        Storage storage = new Storage();
        List<String> lines = storage.readLines("greetings.txt");
        String msg = "Hello! I'm Monika. What is your name?";
        String userName;
        if (lines.get(0).equals("Monika")) {
            ui.say(msg);
            String name = ui.input();
            while (name.isEmpty() || name.equalsIgnoreCase("monika")) {
                ui.say("Oh no! Please try another name!");
                name = ui.input();
            }
            ui.say(String.format("Nice to meet you %s, welcome to the Literature Club! What can I help you today?", name));
            lines.set(0, name);
            storage.writeLines("greetings.txt", lines);
        } else {
            msg = lines.get(1 + new Random().nextInt(lines.size() - 1));
            userName = lines.get(0);
            ui.say(msg.replace("[player]", userName));
        }
    }


    public String getResponse(String input) throws IOException {
        Command cmd = parser.parse(input);
        String s = exe.execute(cmd, tasks);
        if (s == null) {
            storage.storeTasks(tasks);
            System.exit(0);
        }
        return s;
    }
}
