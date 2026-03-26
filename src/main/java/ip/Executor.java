package ip;

import ip.tasks.*;

public class Executor {

    // Store whether the conversation is continued due to a double check on suspicious tasks
    private boolean isContd = false;

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

        // When user did not enter any words (entered nothing or only spaces)
        if (cmd == null || cmd.getCmdType().isEmpty()) {
            return "Don't be shy, say something to me.";
        }

        // Yes/No question asking the user whether to keep the erroneous looking deadline/event
        if (isContd) {
            if (cmd.getCmdType().equalsIgnoreCase("yes")) {
                isContd = false;
                return String.format("Sure, I will add this task to my list as it is:\n%s",
                        taskList.getTask(taskList.getSize() - 1));
            } else if (cmd.getCmdType().equalsIgnoreCase("no")) {
                isContd = false;
                taskList.removeTask(taskList.getSize() - 1);
                return "No problem, that task is ignored.";
            }
            return "Please tell me yes or no.";
        }

        // Handle normal commands with a type and (optional) content
        switch (cmd.getCmdType()) {
            case "bye", "goodbye": {
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
                if (cmd.getContents().length > 0) {
                    return taskList.find(cmd.getContents()[0]);
                }
                return "Please tell me what to find.";
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

                    if (name.isEmpty()) {
                        return "Event should have a description, right?";
                    }

                    Event e = new Event(trimStringBuilder(name), trimStringBuilder(from), trimStringBuilder(to));
                    taskList.addTask(e);

                    return switch (e.timesAreUnderstood()) {
                        case 0 -> String.format("Got it. I've added this event:\n%s", e);
                        case 1 -> {
                            isContd = true;
                            yield "I don't understand the starting time of your event, do you still want to add it as it is?";
                        }
                        case 2 -> {
                            isContd = true;
                            yield "I don't understand the ending time of your event, do you still want to add it as it is?";
                        }
                        case 3 -> {
                            isContd = true;
                            yield "Hmm, I don't understand both times you provided... Do you still want to add the event as it is?";
                        }
                        case 4 -> {
                            taskList.removeTask(taskList.getSize() - 1);
                            yield "Hey, start time can't be after end time.";
                        }
                        default -> throw new RuntimeException("Event state is incorrect.");
                    };
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

                    if (name.isEmpty()) {
                        return "Deadline description can't be empty.";
                    }

                    Deadline d = new Deadline(trimStringBuilder(name), trimStringBuilder(by));
                    taskList.addTask(d);
                    if (d.getTimeIsUnderstood()) {
                        return String.format("I've added this task for you:\n%s", d);
                    }
                    isContd = true;
                    return "I don't understand the time of your deadline, do you still want to add it as it is?";
                }
                return "Hey! The description of a deadline task cannot be empty.";
            }
            default: {
                return "Sorry, I don't know what do you mean.";
            }
        }
    }
}
