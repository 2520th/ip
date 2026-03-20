package ip.test;

import ip.Command;
import ip.Executor;
import ip.TaskMgr;
import ip.tasks.Deadline;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExecutorTest {

    Executor exe = new Executor();

    @Test
    void testAddAndRemoveDeadlineTask() {
        TaskMgr tm = new TaskMgr(1);
        tm.addTask(new Deadline("abc", "2026-3-25"));
        Command cmd = new Command("list", new String[]{});
        assertEquals(exe.execute(cmd, tm), "1. [D] [ ] abc (by: 2026/03/25)");
        assertEquals(tm.getSize(), 1);
        cmd = new Command("delete", new String[]{"1"});
        assertEquals(exe.execute(cmd, tm), "Sure, I've removed this task:\n" +
                "    [D] [ ] abc (by: 2026/03/25)\n" +
                "0 task(s) remaining.");
        assertEquals(tm.getSize(), 0);
        assertNotNull(tm);
    }
}