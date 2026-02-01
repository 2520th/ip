package ip;

import java.time.ZonedDateTime;
import java.util.*;

public class Time {
    private final String original;
    private boolean isUnderstood;
    private int year, month, day, hour, minute;

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

    // Accepts HHMM, HH-MM, YYYY-MM-DD-HHMM(or HH-MM), DD-MM-YYYY-HHMM(or HH-MM)
    public Time(String s) {
        original = s;
        String[] tokens = s.toLowerCase().split("[-/.: ]");
        isUnderstood = false;
        List<Integer> times = toTimes(tokens);
        ZonedDateTime nowWithZone = ZonedDateTime.now();
        year = nowWithZone.getYear();
        month = nowWithZone.getMonthValue();
        day = nowWithZone.getDayOfMonth();
        hour = nowWithZone.getHour();
        minute = nowWithZone.getMinute();
        switch (times.size()) {
            case 1: {
                if (times.getFirst() < 2400) {
                    hour = times.getFirst() / 100;
                    minute = times.getFirst() % 100;
                    isUnderstood = true;
                }
                break;
            }
            case 2: {
                if (times.getFirst() < 24 && times.get(1) < 60) {
                    hour = times.getFirst();
                    minute = times.get(1);
                    isUnderstood = true;
                }
                break;
            }
            case 3: {
                if (times.getFirst() >= year && times.get(1) <= 12 && times.get(2) <= 31) {
                    year = times.getFirst();
                    month = times.get(1);
                    day = times.get(2);
                    isUnderstood = true;
                } else if (times.getFirst() <= 31 && times.get(1) <= 12 && times.get(2) >= year) {
                    day = times.getFirst();
                    month = times.get(1);
                    year = times.get(2);
                    isUnderstood = true;
                }
                break;
            }
            case 4: {
                if (times.getFirst() >= year && times.get(1) <= 12 && times.get(2) <= 31 && times.get(3) < 2400) {
                    year = times.getFirst();
                    month = times.get(1);
                    day = times.get(2);
                    hour = times.get(3) / 100;
                    minute = times.get(3) % 100;
                    isUnderstood = true;
                } else if (times.getFirst() <= 31 && times.get(1) <= 12 && times.get(2) >= year && times.get(3) < 2400) {
                    day = times.getFirst();
                    month = times.get(1);
                    year = times.get(2);
                    hour = times.get(3) / 100;
                    minute = times.get(3) % 100;
                    isUnderstood = true;
                }
                break;
            }
            case 5: {
                if (times.getFirst() >= year && times.get(1) <= 12 && times.get(2) <= 31 && times.get(3) < 24 && times.get(4) < 60) {
                    year = times.getFirst();
                    month = times.get(1);
                    day = times.get(2);
                    hour = times.get(3);
                    minute = times.get(4);
                    isUnderstood = true;
                } else if (times.getFirst() <= 31 && times.get(1) <= 12 && times.get(2) >= year && times.get(3) < 24 && times.get(4) < 60) {
                    day = times.getFirst();
                    month = times.get(1);
                    year = times.get(2);
                    hour = times.get(3);
                    minute = times.get(4);
                    isUnderstood = true;
                }
            }
        }
    }

    @Override
    public String toString() {
        return isUnderstood ? String.format("%s-%s-%s %s:%s", year, month, day, hour, minute) : original;
    }
}
