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

    // 0 = both yes, 1 = from no, 2 = to no, 3 = both no, 4 = invalid due to from after to
    public int timesAreUnderstood() {
        int res = from.getIsUnderstood() ? 0 : 1;
        res += to.getIsUnderstood() ? 0 : 2;
        if (res == 0 && from.getTime().isAfter(to.getTime())) {
            return 4;
        }
        return res;
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