package ip;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

public class Storage {

    private final String DATA_PATH = "data";

    /**
     * Read a text file (in the jar file) and return its content as a list of Strings
     * @param path is the text file path
     * @return List of lines of the file
     * @throws IOException when file is not found
     */
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

    /**
     * Creates data folder in parallel to the fat jar when first used
     * @throws IOException when path does not exist
     */
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

    /**
     * Read a text file from the data folder
     * @param filename is the text file path
     * @return List of lines of the file
     * @throws IOException when file is not found
     */
    public List<String> readLines(String filename) throws IOException {
        Path f = Paths.get(System.getProperty("data.dir", DATA_PATH)).resolve(filename);
        try {
            return Files.readAllLines(f);
        } catch (NoSuchFileException e) {
            throw new IOException(String.format("Oops, can you find the %s file?", filename));
        }
    }

    /**
     * Write to a file in the data folder
     * @param filename is the text file path
     * @param lines is lines of content to write
     * @throws IOException when file is not found
     */
    public void writeLines(String filename, List<String> lines) throws IOException {
        Path f = Paths.get(System.getProperty("data.dir", DATA_PATH)).resolve(filename);
        try {
            Files.write(f, lines);
        } catch (NoSuchFileException e) {
            throw new IOException(String.format("Please don't remove the file %s", filename));
        }
    }

    /**
     * Load tasks.txt file into a TaskMgr, create a new tasks.txt file if not found
     * @return newly loaded TaskMgr object to update in memory
     * @throws IOException when readLines throws IOException
     */
    public TaskMgr loadTasks() throws IOException {
        List<String> lines = readLines("tasks");
        TaskMgr result = new TaskMgr(lines.size() + 16);
        result.addAllTasks(lines);
        return result;
    }

    /**
     * Store tasks into tasks.txt file which guarantees existence due to loadTasks() method
     * @param tm is the TaskMgr with finalized content waiting to be saved
     * @throws IOException when writeLines throw IOException
     */
    public void storeTasks(TaskMgr tm) throws IOException {
        writeLines("tasks", tm.export());
    }
}
