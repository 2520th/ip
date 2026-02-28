package ip;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

public class Time {
    private final String original;
    private boolean isUnderstood;
    private LocalDateTime time;

    // String to valid time-representing int
    private int toNum(String s) {
        if (s.isEmpty() || s.length() > 8) return -1;
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
     * Accept formats: HHMM, HH-MM, YYYYMMDD, YYYY-MM-DD-HHMM(or HH-MM), DD-MM-YYYY-HHMM(or HH-MM)
     * A large constructor here with time identifying logic
     * Assumes date = today if not specified
     * Ignores hour and minute if only date specified
     * Understood time will set isUnderstood to true, otherwise false
     * @param s as a String which contains the correctly formatted String
     */
    public Time(String s) {
        original = s;
        String[] tokens = s.toLowerCase().split("[-/.: ]");
        isUnderstood = false;
        List<Integer> times = toTimes(tokens);
        LocalDateTime now = LocalDateTime.now();
        switch (times.size()) {
            case 1: {
                int h = times.get(0) / 100, m = times.get(0) % 100;
                if (isValidTime(h, m)) {
                    // HHMM
                    time = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), h, m);
                    if (time.isBefore(now)) {
                        time = time.plusDays(1);
                    }
                    isUnderstood = true;
                } else if (times.get(0) > now.getYear() * 10000) {
                    // YYYYMMDD
                    int ty = times.get(0) / 10000, tm = times.get(0) % 10000 / 100, td = tm % 100;
                    if (isValidDate(ty, tm, td)) {
                        time = LocalDateTime.of(ty, tm, td, 0, 0);
                        isUnderstood = true;
                    }
                }
                break;
            }
            case 2: {
                if (isValidTime(times.get(0), times.get(1))) {
                    time = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), times.get(0), times.get(1));
                    if (time.isBefore(now)) {
                        time = time.plusDays(1);
                    }
                    isUnderstood = true;
                }
                break;
            }
            case 3: {
                if (isValidDate(times.get(0), times.get(1), times.get(2))) {
                    time = LocalDateTime.of(times.get(0), times.get(1), times.get(2), 0, 0, 0);
                    isUnderstood = true;
                } else if (isValidDate(times.get(2), times.get(1), times.get(0))) {
                    time = LocalDateTime.of(times.get(2), times.get(1), times.get(0), 0, 0, 0);
                    isUnderstood = true;
                }
                break;
            }
            case 4: {
                int h = times.get(3) / 100, m = times.get(3) % 100;
                if (isValidDate(times.get(0), times.get(1), times.get(2)) && isValidTime(h, m)) {
                    time = LocalDateTime.of(times.get(0), times.get(1), times.get(2), h, m, 0);
                    isUnderstood = true;
                } else if (isValidDate(times.get(2), times.get(1), times.get(0)) && isValidTime(h, m)) {
                    time = LocalDateTime.of(times.get(2), times.get(1), times.get(0), h, m, 0);
                    isUnderstood = true;
                }
                break;
            }
            case 5: {
                if (isValidDate(times.get(0), times.get(1), times.get(2)) && isValidTime(times.get(3), times.get(4))) {
                    time = LocalDateTime.of(times.get(0), times.get(1), times.get(2), times.get(3), times.get(4), 0);
                    isUnderstood = true;
                } else if (isValidDate(times.get(2), times.get(1), times.get(0)) && isValidTime(times.get(3), times.get(4))){
                    time = LocalDateTime.of(times.get(2), times.get(1), times.get(0), times.get(3), times.get(4), 0);
                    isUnderstood = true;
                }
            }
        }
    }

    @Override
    public String toString() {
        return isUnderstood ? time.toString() : original;
    }
}
