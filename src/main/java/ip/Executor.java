package ip;

import ip.tasks.*;

public class Executor {

    /**
     * Treat time-representing StringBuilders from reading tokens after /from, /to, /by sub-commands
     * @param sb takes in StringBuilder containing time info
     * @return correctly trimmed String
     */
    private String trimStringBuilder(StringBuilder sb) {
        if (!sb.isEmpty()) {
            sb.setLength(sb.length() - 1);
            return sb.toString();
        }
        return "Unspecified";
    }

    /**
     * Perform main logic operations here based on command input
     * @param cmd := command type as a String
     * @param taskList := taskMgr array that Monika is currently holding in memory
     * @return execute result as a whole String for Monika to say directly
     */
    public String execute(Command cmd, TaskMgr taskList) {

        if (cmd == null || cmd.getCmdType().isEmpty()) {
            return "Don't be shy, say something to me.";
        }

        switch (cmd.getCmdType()) {
            case "bye", "exit": {
                return null;
            }
            // Intentional fall through here if list is not a command on its own
            case "list": {
                if (cmd.getContents().length == 0) {
                    return taskList.list();
                }
            }
            // Task management matters
            case "mark", "unmark", "delete": {
                int id = 0;
                char type = cmd.getCmdType().charAt(0);
                if (cmd.getContents().length == 0) {
                    return String.format("Emm, please tell me which task to %s.", cmd.getCmdType());
                }

                // Ensures user entering a correct number
                for (int i = 0; i < cmd.getContents()[0].length(); i++) {
                    char c = cmd.getContents()[0].charAt(i);
                    if (c >= '0' && c <= '9') {
                        id = id * 10 + (c & 15);
                    } else {
                        return String.format("Please give me a number after %s.", cmd.getCmdType());
                    }
                }

                String ERR_MSG = "We don't have this task yet. Say \"list\" to view task list.";
                // Tries to delete the chosen task
                if (type == 'd') {
                    Task t = taskList.removeTask(--id);
                    return t == null ? ERR_MSG : String.format("Sure, I've removed this task:\n    " +
                            "%s\n%d task(s) remaining.", t, taskList.getSize());
                }

                // Tries to mark the chosen task
                if (taskList.mark(--id, type == 'm')) {
                    return String.format("OK, I've marked this task as %s:\n%s",
                            type == 'm' ? "done" : "not done yet", taskList.getTask(id));
                }
                return ERR_MSG;
            }
            case "todo": {
                if (cmd.getContents().length > 0) {
                    Todo t = new Todo(String.join(" ", cmd.getContents()));
                    taskList.addTask(t);
                    return String.format("Got it. I've added this task:\n%s", t);
                }
                return "Hey! The description of an todo task cannot be empty.";
            }
            case "find": {
                if (cmd.getContents().length == 1) {
                    return taskList.find(cmd.getContents()[0]);
                }
                return "Please tell me one keyword to find.";
            }
            case "event": {
                if (cmd.getContents().length > 0) {
                    StringBuilder name = new StringBuilder();
                    int i = 0;
                    for (; i < cmd.getContents().length && !cmd.getContents()[i].equals("/from"); i++) {
                        name.append(cmd.getContents()[i]).append(' ');
                    }

                    ++i;
                    StringBuilder from = new StringBuilder();
                    for (; i < cmd.getContents().length && !cmd.getContents()[i].equals("/to"); i++) {
                        from.append(cmd.getContents()[i]).append(' ');
                    }

                    StringBuilder to = new StringBuilder();
                    for (int j = i + 1; j < cmd.getContents().length; j++) {
                        to.append(cmd.getContents()[j]).append(' ');
                    }

                    Event e = new Event(trimStringBuilder(name), trimStringBuilder(from), trimStringBuilder(to));
                    taskList.addTask(e);
                    return String.format("Got it. I've added this event:\n%s", e);
                }
                return "Hey! The description of an event cannot be empty.";
            }
            case "deadline": {
                if (cmd.getContents().length > 0) {
                    StringBuilder name = new StringBuilder();
                    int i = 0;
                    for (; i < cmd.getContents().length && !cmd.getContents()[i].equals("/by"); i++) {
                        name.append(cmd.getContents()[i]).append(' ');
                    }

                    StringBuilder by = new StringBuilder();
                    for (int j = i + 1; j < cmd.getContents().length; j++) {
                        by.append(cmd.getContents()[j]).append(' ');
                    }

                    Deadline d = new Deadline(trimStringBuilder(name), trimStringBuilder(by));
                    taskList.addTask(d);
                    return String.format("I've added this task for you:\n%s", d);
                }
                return "Hey! The description of a deadline task cannot be empty.";
            }
            default: {
                return "Sorry, I don't know what do you mean.";
            }
        }
    }
}
