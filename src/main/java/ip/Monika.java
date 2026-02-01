package ip;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Random;

// IP Week 3 Level 8 branch
// Chat with Monika from Doki Doki Literature Club
public class Monika {

    private static final PrintWriter pw = new PrintWriter(System.out, true);
    private static final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static String userName;

    // UI for saying a string message
    private static void say(String msg) {
        final String LINE = "____________________________________________________________\n";
        pw.println(String.format("%s%s\n%s", LINE, msg, LINE));
    }

    // Treat time-representing StringBuilders from reading tokens after /from, /to, /by sub-commands
    private static String trimTaskStringBuilder(StringBuilder sb) {
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
        return "Unspecified";
    }

    // Adapted some randomized greetings from the mod "Monika After Story" from:
    // https://github.com/Monika-After-Story/MonikaModDev/blob/master/Monika%20After%20Story/game/script-greetings.rpy
    private static void greeting() throws IOException {
        Path f = Paths.get(System.getProperty("data.dir", "data")).resolve("greetings.txt");
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

    // Load tasks files into TaskMgr, create a new tasks.txt file if not found
    private static TaskMgr loadTasks() throws IOException {
        Path f = Paths.get(System.getProperty("data.dir", "data")).resolve("tasks.txt");
        if (!Files.exists(f)) {
            Files.createFile(f);
            return new TaskMgr(16);
        }
        List<String> lines = Files.readAllLines(f);
        TaskMgr result = new TaskMgr(lines.size() * 2 + 1);
        for (String line : lines) {
            Task tmp;
            String[] tokens = line.split("\u001F");
            switch (tokens[0].charAt(0)) {
                case 'T': {
                    tmp = new Todo(tokens[2]);
                    break;
                }
                case 'D': {
                    tmp = new Deadline(tokens[2], tokens[3]);
                    break;
                }
                case 'E': {
                    tmp = new Event(tokens[2], tokens[3], tokens[4]);
                    break;
                }
                default: {
                    tmp = new Task(tokens[1]);
                }
            }
            tmp.mark(tokens[1].charAt(0) == 'X');
            result.addTask(tmp);
        }
        return result;
    }

    // Store tasks into tasks.txt file which guarantees existence due to loadTasks() method
    private static void storeTasks(TaskMgr tm) throws IOException {
        Path f = Paths.get(System.getProperty("data.dir", "data")).resolve("tasks.txt");
        Files.write(f, tm.export());
    }

    public static void main(String[] args) throws IOException {
        greeting();
        TaskMgr tasks = loadTasks();
        WHILE:
        while (true) {
            String input = br.readLine().trim();
            String[] tokens = input.split(" ");
            SWITCH:
            switch (tokens[0].toLowerCase()) {
                case "": {
                    say("Say something to me.");
                    break;
                }
                case "bye": {
                    break WHILE;
                }
                case "list": {
                    if (tokens.length == 1) {
                        say(tasks.list());
                        break;
                    }
                }
                // Task management matters
                case "mark", "unmark", "delete": {
                    int id = 0;
                    char type = tokens[0].charAt(0);
                    if (tokens.length == 1) {
                        say(String.format("Emm, please tell me which task to %s.", tokens[0]));
                        break;
                    }
                    for (int i = 0; i < tokens[1].length(); i++) {
                        char c = tokens[1].charAt(i);
                        if (c >= '0' && c <= '9') {
                            id = id * 10 + (c & 15);
                        } else {
                            say(String.format("Please give me a number after %s.", tokens[0]));
                            break SWITCH;
                        }
                    }
                    String ERR_MSG = "We don't have this task yet. Say \"list\" to view task list.";
                    if (type == 'd') {
                        Task t = tasks.removeTask(--id);
                        say(t == null ? ERR_MSG : String.format("Sure, I've removed this task:\n    " +
                                "%s\n%d task(s) remaining.", t, tasks.getSize()));
                        break;
                    }
                    if (tasks.mark(--id, type == 'm')) {
                        say(String.format("OK, I've marked this task as %s:\n%s",
                                type == 'm' ? "done" : "not done yet", tasks.getTask(id)));
                    } else {
                        say(ERR_MSG);
                    }
                    break;
                }
                case "todo": {
                    if (tokens.length > 1) {
                        Todo t = new Todo(input.substring(5));
                        tasks.addTask(t);
                        say(String.format("Got it. I've added this task:\n%s", t));
                    } else {
                        say("Hey! The description of an todo task cannot be empty.");
                    }
                    break;
                }
                case "event": {
                    if (tokens.length > 1) {
                        StringBuilder name = new StringBuilder();
                        StringBuilder from = new StringBuilder();
                        StringBuilder to = new StringBuilder();
                        for (int i = 1; i < tokens.length; i++) {
                            if (tokens[i].equals("/from")) {
                                for (int j = i + 1; j < tokens.length; j++) {
                                    if (tokens[j].equals("/to")) {
                                        for (int k = j + 1; k < tokens.length; k++) {
                                            to.append(tokens[k]).append(' ');
                                        }
                                        break;
                                    }
                                    from.append(tokens[j]).append(' ');
                                }
                                break;
                            }
                            name.append(tokens[i]).append(' ');
                        }
                        name.deleteCharAt(name.length() - 1);
                        Event e = new Event(name.toString(), trimTaskStringBuilder(from), trimTaskStringBuilder(to));
                        tasks.addTask(e);
                        say(String.format("Got it. I've added this event:\n%s", e));
                    } else {
                        say("Hey! The description of an event cannot be empty.");
                    }
                    break;
                }
                case "deadline": {
                    if (tokens.length > 1) {
                        StringBuilder name = new StringBuilder();
                        StringBuilder by = new StringBuilder();
                        for (int i = 1; i < tokens.length; i++) {
                            if (tokens[i].equals("/by")) {
                                for (int j = i + 1; j < tokens.length; j++) {
                                    by.append(tokens[j]).append(' ');
                                }
                                break;
                            }
                            name.append(tokens[i]).append(' ');
                        }
                        name.deleteCharAt(name.length() - 1);
                        Deadline d = new Deadline(name.toString(), trimTaskStringBuilder(by));
                        tasks.addTask(d);
                        say(String.format("I've added this task for you:\n%s", d));
                    } else {
                        say("Hey! The description of a deadline task cannot be empty.");
                    }
                    break;
                }
                default: {
                    say("Sorry, I don't know what do you mean.");
                }
            }
        }
        storeTasks(tasks);
        say(" Bye. Hope to see you again soon!");
        pw.close();
    }
}
