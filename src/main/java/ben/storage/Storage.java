package ben.storage;

import ben.BenException;
import ben.task.Deadline;
import ben.task.Event;
import ben.task.Task;
import ben.task.ToDo;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Paths;

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
        File file = new File(filePath);

        // Create directory if it doesn't exist
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Return empty list if file doesn't exist
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try  {
            // Use Streams to read, filter, and parse lines
            List<Task> tasks = Files.lines(Paths.get(filePath))
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(this::parseTaskSafely)
                    .filter(task -> task != null)
                    .collect(Collectors.toList());

            return new ArrayList<>(tasks);
        } catch (IOException e) {
            throw new BenException("Error loading tasks from file: " + e.getMessage());
        }
    }

    /**
     * Safely parses a task line, returning null if parsing fails.
     * This helper method allows the Stream to continue processing even if some lines are corrupted.
     */
    private Task parseTaskSafely(String line) {
        try {
            return parseTask(line);
        } catch (BenException e) {
            System.err.println("Warning: Skipping corrupted task entry: " + line);
            return null;
        }
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

        try {
            // Use Streams to format all tasks and write them to file
            List<String> formattedTasks = tasks.stream()
                    .map(this::formatTask)
                    .collect(Collectors.toList());

            Files.write(Paths.get(filePath), formattedTasks);
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
                        // Parse the date from ben.storage format
                        LocalDate byDate = LocalDate.parse(parts[3], DATE_STORAGE_FORMAT);
                        task = new Deadline(description, byDate);
                    }
                    break;
                case "E":
                    if (parts.length >= 5) {
                        task = new Event(description, parts[3], parts[4]);
                    }
                    break;
            }
        } catch (Exception e) {
            // Skip corrupted entries
            System.err.println("Warning: Skipping corrupted ben.task entry: " + line);
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