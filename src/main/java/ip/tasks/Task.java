package ip.tasks;

public class Task {
    private final String name;
    private boolean isDone = false;

    public Task(String name) {
        this.name = name;
    }

    public void mark(boolean done) {
        this.isDone = done;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("[%c] %s", isDone ? 'X' : ' ', name);
    }

    /**
     * Convert to string for txt file storage purpose
     * Use 001F as a robust separator
     * Inherited by child classes of Task
     * @return String to be directly store in disk file
     */
    public String record() {
        return String.format("%c\u001F%s", isDone ? 'X' : ' ', name);
    }
}
