import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public List<Task> load() throws OrionException {
        List<Task> tasks = new ArrayList<>();
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                return tasks;
            }
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String[] parts = scanner.nextLine().split(" \\| ");
                String taskType = parts[0];
                boolean isDone = parts[1].equals("1");
                String description = parts[2];
                switch (taskType) {
                    case "T":
                        tasks.add(new Todo(description, isDone));
                        break;

                    case "D":
                        String deadline = parts[3];
                        tasks.add(new Deadline(description, deadline, isDone));
                        break;

                    case "E":
                        String start = parts[3];
                        String end = parts[4];
                        tasks.add(new Event(description, start, end, isDone));
                        break;
                }
            }
        } catch (IOException e) {
            throw new OrionException("There was an error loading tasks from the save file.");
        }
        return tasks;
    }

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