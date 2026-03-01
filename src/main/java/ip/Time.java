package ip;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class Time {
    private final String original;
    private boolean isUnderstood;
    private boolean isAccurate;
    private LocalDateTime time;
    private final Map<String, DayOfWeek> days = Map.of(
            "mon", DayOfWeek.MONDAY,
            "tue", DayOfWeek.TUESDAY,
            "wed", DayOfWeek.WEDNESDAY,
            "thu", DayOfWeek.THURSDAY,
            "fri", DayOfWeek.FRIDAY,
            "sat", DayOfWeek.SATURDAY,
            "sun", DayOfWeek.SUNDAY
    );
    private final Map<String, Integer> months = Map.ofEntries(
            Map.entry("jan", 1),
            Map.entry("feb", 2),
            Map.entry("mar", 3),
            Map.entry("apr", 4),
            Map.entry("may", 5),
            Map.entry("jun", 6),
            Map.entry("jul", 7),
            Map.entry("aug", 8),
            Map.entry("sep", 9),
            Map.entry("oct", 10),
            Map.entry("nov", 11),
            Map.entry("dec", 12)
    );

    // String to valid time-representing int
    private int toNum(String s) {
        if (s.isEmpty() || s.length() > 8) return -1;
        if (s.length() >= 3) {
            String ss = s.substring(0, 3);
            if (months.containsKey(ss)) return months.get(ss);
        }
        int res = 0;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return -1;
            }
            res = res * 10 + s.charAt(i) - '0';
        }
        return res;
    }

    // Parse and filter tokenized string words to purely integers representing time
    private List<Integer> toTimes(String[] tokens) {
        List<Integer> res = new ArrayList<>();
        for (String token : tokens) {
            int t = toNum(token);
            if (t != -1) {
                res.add(t);
            }
        }
        return res;
    }

    boolean isValidDate(int year, int month, int day) {
        if (month < 1 || month > 12) return false;
        return YearMonth.of(year, month).isValidDay(day);
    }

    boolean isValidTime(int hour, int minute) {
        return hour >= 0 && hour < 24 && minute >= 0 && minute < 60;
    }

    /**
     * Accept formats: *day (enter 3 letter abbreviations * = mon, tue, ... or full word = monday, tuesday, ...)
     * HHmm, HH:mm, MM-DD, YYYYMMDD, YYYYMMDD-HHmm, YYYY-MM-DD, DD-MM-YYYY, YYYYMMDD-HH-mm, YYYY-MM-DD-HHmm(or HH-mm), DD-MM-YYYY-HHmm(or HH-mm)
     * For 2 tokens, [: and other separators] are treated differently
     * This is a large constructor with time identifying logic
     * Assumes date = today (or tomorrow if provided time is before now) if not specified
     * Ignores hour and minute if only date specified
     * Understood time will set isUnderstood to true, otherwise false
     * @param s as a String which contains the correctly formatted String
     */
    public Time(String s) {
        original = s;
        s = s.toLowerCase();
        if (s.length() > 5 && s.endsWith("day")) {
            s = s.substring(0, 3);
        }
        if (days.containsKey(s)) {
            isUnderstood = true;
            time = LocalDateTime.now().with(TemporalAdjusters.next(days.get(s)));
            return;
        }
        String[] tokens = s.split("[-/.: ]");
        isAccurate = isUnderstood = false;
        List<Integer> times = toTimes(tokens);
        LocalDateTime now = LocalDateTime.now();
        switch (times.size()) {
            case 1: {
                int h = times.get(0) / 100, m = times.get(0) % 100;
                if (isValidTime(h, m)) {
                    // HHmm
                    time = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), h, m);
                    if (time.isBefore(now)) {
                        time = time.plusDays(1);
                    }
                    isAccurate = isUnderstood = true;
                } else if (times.get(0) > now.getYear() * 10000) {
                    // YYYYMMDD
                    int ty = times.get(0) / 10000, tm = times.get(0) % 10000, td = tm % 100;
                    tm /= 100;
                    if (isValidDate(ty, tm, td)) {
                        time = LocalDateTime.of(ty, tm, td, 0, 0);
                        isUnderstood = true;
                    }
                }
                break;
            }
            case 2: {
                if (s.contains(":")) {
                    // HH:mm
                    if (isValidTime(times.get(0), times.get(1))) {
                        time = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), times.get(0), times.get(1));
                        if (time.isBefore(now)) {
                            time = time.plusDays(1);
                        }
                        isAccurate = isUnderstood = true;
                    }
                } else {
                    // YYYYMMDD-HHmm
                    if (times.get(0) > now.getYear() * 10000) {
                        int y = times.get(0) / 10000, M = times.get(0) % 10000, d = M % 100, h = times.get(1) / 100, m = times.get(1) % 100;
                        M /= 100;
                        if (isValidDate(y, M, d) && isValidTime(h, m)) {
                            time = LocalDateTime.of(y, M, d, h, m);
                            isAccurate = isUnderstood = true;
                        }
                        break;
                    }
                    // MM-DD
                    int y = now.getYear(), m = times.get(0), d = times.get(1);
                    if (isValidDate(y, m, d) && LocalDateTime.of(y, m, d, 0, 0).isAfter(LocalDateTime.now())) {
                        time = LocalDateTime.of(y, m, d, 0, 0);
                        isUnderstood = true;
                    } else if (isValidDate(y + 1, m, d)) {
                        time = LocalDateTime.of(y + 1, m, d, 0, 0);
                        isUnderstood = true;
                    }
                }
                break;
            }
            case 3: {
                if (isValidDate(times.get(0), times.get(1), times.get(2))) {
                    // YYYY-MM-DD
                    time = LocalDateTime.of(times.get(0), times.get(1), times.get(2), 0, 0, 0);
                    isUnderstood = true;
                } else if (isValidDate(times.get(2), times.get(1), times.get(0))) {
                    // DD-MM-YYYY
                    time = LocalDateTime.of(times.get(2), times.get(1), times.get(0), 0, 0, 0);
                    isUnderstood = true;
                } else if (times.get(0) > now.getYear() * 10000) {
                    // YYYYMMDD-HH-mm
                    int y = times.get(0) / 10000, M = times.get(0) % 10000, d = M % 100, h = times.get(1), m = times.get(2);
                    M /= 100;
                    if (isValidDate(y, M, d) && isValidTime(h, m)) {
                        time = LocalDateTime.of(y, M, d, h, m);
                        isAccurate = isUnderstood = true;
                    }
                }
                break;
            }
            case 4: {
                int h = times.get(3) / 100, m = times.get(3) % 100;
                if (isValidDate(times.get(0), times.get(1), times.get(2)) && isValidTime(h, m)) {
                    // YYYY-MM-DD-HHmm
                    time = LocalDateTime.of(times.get(0), times.get(1), times.get(2), h, m, 0);
                    isAccurate = isUnderstood = true;
                } else if (isValidDate(times.get(2), times.get(1), times.get(0)) && isValidTime(h, m)) {
                    // DD-MM-YYYY-HHmm
                    time = LocalDateTime.of(times.get(2), times.get(1), times.get(0), h, m, 0);
                    isAccurate = isUnderstood = true;
                }
                break;
            }
            case 5: {
                if (isValidDate(times.get(0), times.get(1), times.get(2)) && isValidTime(times.get(3), times.get(4))) {
                    // YYYY-MM-DD-HH-mm
                    time = LocalDateTime.of(times.get(0), times.get(1), times.get(2), times.get(3), times.get(4), 0);
                    isAccurate = isUnderstood = true;
                } else if (isValidDate(times.get(2), times.get(1), times.get(0)) && isValidTime(times.get(3), times.get(4))){
                    // DD-MM-YYYY-HH-mm
                    time = LocalDateTime.of(times.get(2), times.get(1), times.get(0), times.get(3), times.get(4), 0);
                    isAccurate = isUnderstood = true;
                }
            }
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(isAccurate ? "yyyy/MM/dd HH:mm" : "yyyy/MM/dd");
        return isUnderstood ? time.format(dtf) : original;
    }
}
