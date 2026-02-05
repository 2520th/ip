package ip;

import java.io.*;
import java.util.List;
import java.util.Random;

// IP Week 3 Level 8: add OOP
// Chat with Monika from Doki Doki Literature Club
public class Monika {

    private static String userName;

    // Adapted some randomized greetings from the mod "Monika After Story" from:
    // https://github.com/Monika-After-Story/MonikaModDev/blob/master/Monika%20After%20Story/game/script-greetings.rpy
    private static void greeting(UI ui) throws IOException {
        Storage storage = new Storage();
        List<String> lines = storage.readLines("greetings.txt");
        String msg = "Hello! I'm Monika. What is your name?";
        if (lines.getFirst().equals("Monika")) {
            ui.say(msg);
            String name = ui.input();
            while (name.isEmpty() || name.equalsIgnoreCase("monika")) {
                ui.say("Oh no! Please try another name!");
                name = ui.input();
            }
            ui.say(String.format("Nice to meet you %s, welcome to the Literature Club! What can I help you today?", name));
            lines.set(0, name);
            storage.writeLines("greetings.txt", lines);
            userName = name;
        } else {
            msg = lines.get(1 + new Random().nextInt(lines.size() - 1));
            userName = lines.getFirst();
            ui.say(msg.replace("[player]", userName));
        }
    }

    public static void main(String[] args) throws IOException {
        UI ui = new UI();
        greeting(ui);
        Storage storage = new Storage();
        TaskMgr tasks = storage.loadTasks();
        Parser parser = new Parser();
        Executor exe = new Executor();
        while (true) {
            Command cmd = parser.parse(ui.input());
            String s = exe.execute(cmd, tasks);
            if (s == null) {
                break;
            } else {
                ui.say(s);
            }
        }
        storage.storeTasks(tasks);
        ui.say(" Bye. Hope to see you again soon!");
    }
}
