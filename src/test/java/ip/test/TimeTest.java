package ip.test;

import ip.Time;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeTest {

    @Test
    void testValidHHmmFormat() {
        Time t = new Time("2359");
        assertTrue(t.getIsUnderstood());
        assertNotNull(t.getTime());
        assertEquals(23, t.getTime().getHour());
        assertEquals(59, t.getTime().getMinute());
    }

    @Test
    void testValidHHColonmmFormat() {
        Time t = new Time("14:30");
        assertTrue(t.getIsUnderstood());
        assertEquals(14, t.getTime().getHour());
        assertEquals(30, t.getTime().getMinute());
    }

    @Test
    void testInvalidTime() {
        Time t = new Time("25:99");
        assertFalse(t.getIsUnderstood());
        assertNull(t.getTime());
    }

    @Test
    void testValidYYYYMMDD() {
        Time t = new Time("20251225");
        assertTrue(t.getIsUnderstood());
        assertEquals(2025, t.getTime().getYear());
        assertEquals(12, t.getTime().getMonthValue());
        assertEquals(25, t.getTime().getDayOfMonth());
    }

    @Test
    void testValidYYYYMMDD_HHmm() {
        Time t = new Time("20251225-1430");
        assertTrue(t.getIsUnderstood());
        assertEquals(14, t.getTime().getHour());
        assertEquals(30, t.getTime().getMinute());
    }

    @Test
    void testValidYYYY_MM_DD() {
        Time t = new Time("2025-12-25");
        assertTrue(t.getIsUnderstood());
        assertEquals(2025, t.getTime().getYear());
        assertEquals(12, t.getTime().getMonthValue());
        assertEquals(25, t.getTime().getDayOfMonth());
    }

    @Test
    void testValidDD_MM_YYYY() {
        Time t = new Time("25-12-2025");
        assertTrue(t.getIsUnderstood());
        assertEquals(2025, t.getTime().getYear());
        assertEquals(12, t.getTime().getMonthValue());
        assertEquals(25, t.getTime().getDayOfMonth());
    }

    @Test
    void testDayOfWeekParsing() {
        Time t = new Time("mon");
        assertTrue(t.getIsUnderstood());
        assertNotNull(t.getTime());
        assertEquals(1, t.getTime().getDayOfWeek().getValue()); // Monday
    }

    @Test
    void testFullDayNameParsing() {
        Time t = new Time("monday");
        assertTrue(t.getIsUnderstood());
    }

    @Test
    void testInvalidInputString() {
        Time t = new Time("not a time");
        assertFalse(t.getIsUnderstood());
        assertNull(t.getTime());
    }

    @Test
    void testToStringAccurateFormat() {
        Time t = new Time("2025-12-25-14-30");
        assertTrue(t.getIsUnderstood());
        String result = t.toString();
        assertTrue(result.contains("2025/12/25"));
        assertTrue(result.contains("14:30"));
    }

    @Test
    void testToStringDateOnly() {
        Time t = new Time("2025-12-25");
        assertTrue(t.getIsUnderstood());
        String result = t.toString();
        assertEquals("2025/12/25", result);
    }

    @Test
    void testMMDDFutureDate() {
        Time t = new Time("12-31");
        assertTrue(t.getIsUnderstood());
        assertEquals(12, t.getTime().getMonthValue());
        assertEquals(31, t.getTime().getDayOfMonth());
    }

    @Test
    void testInvalidDate() {
        Time t = new Time("2025-02-30");
        assertFalse(t.getIsUnderstood());
    }
}