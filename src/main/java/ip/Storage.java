package ip;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class Storage {

    private final String DATA_PATH = "data";

    public List<String> readLines(String filename) throws IOException {
        Path f = Paths.get(System.getProperty("data.dir", DATA_PATH)).resolve(filename);
        try {
            return Files.readAllLines(f);
        } catch (NoSuchFileException e) {
            throw new IOException(String.format("Oops, can you find the %s file?", filename));
        }
    }

    public void writeLines(String filename, List<String> lines) throws IOException {
        Path f = Paths.get(System.getProperty("data.dir", DATA_PATH)).resolve(filename);
        try {
            Files.write(f, lines);
        } catch (NoSuchFileException e) {
            throw new IOException(String.format("Please don't remove the file %s", filename));
        }
    }

    // Load tasks.txt file into a TaskMgr, create a new tasks.txt file if not found
    public TaskMgr loadTasks() throws IOException {
        List<String> lines = readLines("tasks.txt");
        TaskMgr result = new TaskMgr(lines.size() * 2 + 16);
        result.addAllTasks(lines);
        return result;
    }

    // Store tasks into tasks.txt file which guarantees existence due to loadTasks() method
    public void storeTasks(TaskMgr tm) throws IOException {
        writeLines("tasks.txt", tm.export());
    }
}
