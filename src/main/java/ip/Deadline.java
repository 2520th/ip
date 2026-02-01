package ip;

public class Deadline extends Task{

    private final Time time;

    public Deadline(String name, String t) {
        super(name);
        time = new Time(t);
    }

    @Override
    public String toString() {
        return String.format("[D] %s (by: %s)", super.toString(), time);
    }

    @Override
    public String record() {
        return String.format("D\u001F%s\u001F%s", super.record(), time);
    }
}
