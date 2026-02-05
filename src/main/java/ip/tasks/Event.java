package ip.tasks;

import ip.Time;

public class Event extends Task {

    private final Time from;
    private final Time to;

    public Event(String name, String f, String t) {
        super(name);
        from = new Time(f);
        to = new Time(t);
    }

    @Override
    public String toString() {
        return String.format("[E] %s (from: %s, to: %s)", super.toString(), from, to);
    }

    @Override
    public String record() {
        return String.format("E\u001F%s\u001F%s\u001F%s", super.record(), from, to);
    }
}