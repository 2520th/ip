package ip;

public class Todo extends Task {
    public Todo(String name) {
        super(name);
    }

    public String toString() {
        return String.format("[T] %s", super.toString());
    }

    public String record() {
        return String.format("T\u001F%s", super.record());
    }
}