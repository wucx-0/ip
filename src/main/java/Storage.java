import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> loadTasks() throws BenException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTasks(line.trim());
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new BenException("Error loading tasks from file: " + e.getMessage());
        }

        return tasks;
    }

    public void saveTasks(ArrayList<Task> tasks) throws BenException {
        File file = new File(filePath);

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(file)) {
            for (Task task : tasks) {
                writer.println(formatTask(task));
            }
        } catch (IOException e) {
            throw new BenException("Error saving tasks to file: " + e.getMessage());
        }
    }

    private Task parseTasks(String line) throws BenException {
        if (line.isEmpty()) {
            return null;
        }

        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task = null;

        switch (type) {
            case "T":
                task = new ToDo(description);
                break;
            case "D":
                if (parts.length >= 4) {
                    task = new Deadline(description, parts[3]);
                }
                break;
            case "E":
                if (parts.length >= 5) {
                    task = new Event(description, parts[3], parts[4]);
                }
                break;
        }

        if (task != null && isDone) {
            task.markComplete();
        }

        return task;
    }

    private String formatTask(Task task) {
        String status = task.isComplete() ? "1" : "0";
        String type = task.getType();
        String description = task.getDescription();

        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return type + " | " + status + " | " + description + " | " + deadline.getDeadline();
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return type + " | " + status + " | " + description + " | " + event.getFrom() + " | " + event.getTo();
        } else {
            return type + " | " + status + " | " + description;
        }
    }
}
