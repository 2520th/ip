package ip;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class Storage {

    private final String DATA_PATH = "data";

    public List<String> getLines(String path) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new IOException("Resource not found: " + path);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.toList());
            }
        }
    }

    public void setupDataFolder() throws IOException {
        Path dataDir = Paths.get("data");
        if (Files.notExists(dataDir)) {
            Files.createDirectory(dataDir);
        }
        Path targetPath = dataDir.resolve("tasks");
        if (Files.notExists(targetPath)) {
            Files.createFile(targetPath);
        }
        targetPath = dataDir.resolve("data");
        if (Files.notExists(targetPath)) {
            Files.createFile(targetPath);
            Files.writeString(targetPath, "Monika\n0");
        }
    }

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
        List<String> lines = readLines("tasks");
        TaskMgr result = new TaskMgr(lines.size() * 2 + 16);
        result.addAllTasks(lines);
        return result;
    }

    // Store tasks into tasks.txt file which guarantees existence due to loadTasks() method
    public void storeTasks(TaskMgr tm) throws IOException {
        writeLines("tasks", tm.export());
    }
}
