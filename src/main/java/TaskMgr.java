// Task Manager Class
public class TaskMgr {
    // Dynamic array data structure
    private Task[] tasks;
    private int arrLen;

    public TaskMgr(int size) {
        tasks = new Task[size];
    }

    public void addTask(Task task) {
        if (arrLen == tasks.length) {
            Task[] newTasks = new Task[arrLen * 2];
            System.arraycopy(tasks, 0, newTasks, 0, arrLen);
            tasks = newTasks;
        }
        tasks[arrLen++] = task;
    }

    public Task getTask(int id) {
        return tasks[id];
    }

    public boolean mark(int id, boolean done) {
        if (id >= 0 && id < arrLen) {
            tasks[id].mark(done);
            return true;
        }
        return false;
    }

    public int getSize() {
        return arrLen;
    }

    public Task removeTask(int id) {
        if (id >= 0 && id < arrLen) {
            Task t = tasks[id];
            System.arraycopy(tasks, id + 1, tasks, id, --arrLen - id);
            return t;
        }
        return null;
    }

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
}
