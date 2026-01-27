public class Task {
    private final String name;
    protected boolean isDone = false;

    public Task(String name) {
        this.name = name;
    }

    public void mark(boolean done) {
        this.isDone = done;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return String.format("[%c] %s", isDone ? 'X' : ' ', name);
    }
}
