package ip;

import ip.tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


// JUnit Test Increment class
public class MonikaTest {
    @Test
    public void testExecutor() {
        Executor exe = new Executor();
        Command cmd = new Command("list", new String[]{});
        TaskMgr tm = new TaskMgr(1);
        tm.addTask(new Task("0123"));
        assertEquals(exe.execute(cmd, tm), "1. [ ] 0123");
        tm.removeTask(0);
        assertEquals(exe.execute(cmd, tm), "We don't have any tasks yet...");
        cmd = new Command("list", new String[]{"some tasks"});
        assertEquals(exe.execute(cmd, tm), "Please give me a number after list.");
    }

    @Test
    public void testTime() {
        assertEquals(new Time("2035-09-25 12:34").toString(), "2035-9-25 12:34");
        assertEquals(new Time("20350925").toString(), "2035-9-25");
    }
}