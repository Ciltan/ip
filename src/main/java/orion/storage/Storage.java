package orion.storage;

import orion.exception.OrionException;
import orion.task.Deadline;
import orion.task.Event;
import orion.task.Task;
import orion.task.Todo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the loading and saving of task data to a file.
 */
public class Storage {
    private String filePath;
    public static final DateTimeFormatter SAVE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads the tasks from the save file.
     *
     * @return A list of tasks parsed from the save file.
     * @throws OrionException If there is an error reading the file or parsing the data.
     */
    public List<Task> load() throws OrionException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                return tasks;
            }
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                try {
                    String[] parts = line.split(" \\| ");
                    String taskType = parts[0];
                    boolean isDone = parts[1].equals("1");
                    String description = parts[2];

                    switch (taskType) {
                        case "T":
                            tasks.add(new Todo(description, isDone));
                            break;

                        case "D":
                            LocalDateTime deadline = LocalDateTime.parse(parts[3], SAVE_FORMAT);
                            tasks.add(new Deadline(description, deadline, isDone));
                            break;

                        case "E":
                            LocalDateTime start = LocalDateTime.parse(parts[3], SAVE_FORMAT);
                            LocalDateTime end = LocalDateTime.parse(parts[4], SAVE_FORMAT);
                            tasks.add(new Event(description, start, end, isDone));
                            break;

                        default:
                            System.out.println("Unknown task type found in save file. Skipping line: " + line);
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Corrupted data found in save file. Skipping line:" + line);
                }
            }
        } catch (IOException e) {
            throw new OrionException("There was an error loading tasks from the save file.");
        }
        return tasks;
    }

    /**
     * Saves the current list of tasks to the save file.
     *
     * @param tasks The list of tasks to be saved.
     * @throws OrionException If there is an error writing the tasks to the file.
     */
    public void save(List<Task> tasks) throws OrionException {
        try {
            File file = new File(filePath);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            FileWriter fw = new FileWriter(filePath);
            for (Task task : tasks) {
                fw.write(task.toFileFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            throw new OrionException("There was an error saving tasks to the save file.");
        }
    }
}