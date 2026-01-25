public class Event extends Task {

    private final String from;
    private final String to;

    public Event(String name, String f, String t) {
        super(name);
        from = f;
        to = t;
    }

    public String toString() {
        return String.format("[E] %s (from: %s, to: %s)", super.toString(), from, to);
    }
}