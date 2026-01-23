public class Task {
    private final String name;
    private boolean done = false;

    public Task(String name) {
        this.name = name;
    }

    public void mark(boolean done) {
        this.done = done;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return String.format("[%c] %s", done ? 'X' : ' ', name);
    }
}
