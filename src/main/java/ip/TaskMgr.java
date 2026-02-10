package ip;

import java.util.ArrayList;
import java.util.List;
import ip.tasks.*;

// Task Manager Class (contains the task list)
public class TaskMgr {
    // Dynamic array data structure
    private Task[] tasks;
    private int arrLen;

    public TaskMgr(int size) {
        tasks = new Task[size];
    }

    /**
     * Adds a single task to TaskMgr at amortized O(1) time
     * Expands by a factor of 2, similar to ArrayList implementation
     * @param task is the task to add to the data structure.
     */
    public void addTask(Task task) {
        if (arrLen == tasks.length) {
            Task[] newTasks = new Task[arrLen * 2];
            System.arraycopy(tasks, 0, newTasks, 0, arrLen);
            tasks = newTasks;
        }
        tasks[arrLen++] = task;
    }

    /**
     * Takes in a list of strings read from tasks.txt and put it into a new TaskMgr
     * Takes care of decode and classification of tasks when adding as String
     * @param lines comes from NIO read all lines method
     */
    public void addAllTasks(List<String> lines) {
        for (String line : lines) {
            Task tmp;
            String[] tokens = line.split("\u001F");
            // Read format of tasks retrieve from tasks.txt
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
            addTask(tmp);
        }
    }

    // Just a getter as usual
    public Task getTask(int id) {
        return tasks[id];
    }

    /**
     * Mark event number [id] as [isDone]
     * @param id := event number in the array
     * @param isDone := mark vs unmark specification
     * @return marking status (success = true)
     */
    public boolean mark(int id, boolean isDone) {
        if (id >= 0 && id < arrLen) {
            tasks[id].mark(isDone);
            return true;
        }
        return false;
    }

    // Return number of tasks
    public int getSize() {
        return arrLen;
    }

    /**
     * Simple O(n) removal from ordered array, return removed task
     * @param id := event number in the array
     * @return removed task if successful, else null
     */
    public Task removeTask(int id) {
        if (id >= 0 && id < arrLen) {
            Task t = tasks[id];
            System.arraycopy(tasks, id + 1, tasks, id, --arrLen - id);
            return t;
        }
        return null;
    }

    // Returns a whole list as a single string for Monika to message at once
    public String list() {
        if (arrLen == 0) {
            return "We don't have any tasks yet...";
        }
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < arrLen; i++) {
            res.append(i + 1).append(". ").append(tasks[i].toString()).append('\n');
        }
        res.deleteCharAt(res.length() - 1);
        return res.toString();
    }

    /**
     * find method for user to search a keyword for relevant tasks
     * @param pattern is what the user wants to find
     * @return a whole String containing a list of matched strings for Monika to say
     */
    public String find(String pattern) {
        StringBuilder res = new StringBuilder();
        int index = 0;
        res.append("Here are the matching tasks in your list:\n");
        for (int i = 0; i < arrLen; i++) {
            if (tasks[i].getName().contains(pattern)) {
                res.append(++index).append(". ").append(tasks[i]).append('\n');
            }
        }
        return res.toString();
    }

    // Export all tasks' records to a List of Strings ready to write in tasks.txt file
    public List<String> export() {
        List<String> res = new ArrayList<>(arrLen);
        for (int i = 0; i < arrLen; ++i) {
            res.add(tasks[i].record());
        }
        return res;
    }
}
