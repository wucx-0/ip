package ben.storage;

import ben.BenException;
import ben.task.Deadline;
import ben.task.Event;
import ben.task.Task;
import ben.task.ToDo;
import java.time.LocalDateTime;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Manages persistent storage of tasks to and from files.
 */
public class Storage {
    private String filePath;
    private static final DateTimeFormatter DATE_STORAGE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the configured file path.
     * Creates necessary directories if they don't exist, returns empty list if file doesn't exist.
     *
     * @return list of tasks loaded from storage file
     * @throws BenException if file reading fails or data is corrupted
     */
    public ArrayList<Task> loadTasks() throws BenException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        // Create directory if it doesn't exist
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Return empty list if file doesn't exist
        if (!file.exists()) {
            return tasks;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTask(line.trim());
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new BenException("Error loading tasks from file: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Saves all tasks to the configured file path in the specified format.
     * Creates necessary directories and overwrites existing file content.
     *
     * @param tasks the list of tasks to save to storage
     * @throws BenException if file writing fails or directory creation fails
     */
    public void saveTasks(ArrayList<Task> tasks) throws BenException {
        File file = new File(filePath);

        // Create directory if it doesn't exist
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

    private Task parseTask(String line) throws BenException {
        if (line.isEmpty()) {
            return null;
        }

        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            // Handle corrupted data - skip this line
            return null;
        }

        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task = null;

        try {
            switch (type) {
            case "T":
                task = new ToDo(description);
                break;
            case "D":
                if (parts.length >= 4) {
                    // Parse the date from storage format
                    LocalDate byDate = LocalDate.parse(parts[3], DATE_STORAGE_FORMAT);
                    task = new Deadline(description, byDate);
                }
                break;
            case "E":
                if (parts.length >= 5) {
                    // Parse LocalDateTime directly from storage format
                    // The storage saves LocalDateTime.toString() which produces ISO format
                    LocalDateTime startDateTime = LocalDateTime.parse(parts[3]);
                    LocalDateTime endDateTime = LocalDateTime.parse(parts[4]);
                    task = new Event(description, startDateTime, endDateTime);
                }
                break;
            }
        } catch (Exception e) {
            // Skip corrupted entries
            System.err.println("Warning: Skipping corrupted task entry: " + line + " - Error: " + e.getMessage());
            return null;
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
            // Save date in ben.storage format (yyyy-MM-dd)
            String dateString = deadline.getDeadline().format(DATE_STORAGE_FORMAT);
            return type + " | " + status + " | " + description + " | " + dateString;
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return type + " | " + status + " | " + description + " | " + event.getStartTime() + " | " + event.getEndTime();
        } else {
            return type + " | " + status + " | " + description;
        }
    }
}