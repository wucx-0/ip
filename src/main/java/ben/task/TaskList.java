package ben.task;

import ben.BenException;
import ben.storage.Storage;
import ben.ui.UI;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;
    private Storage storage;
    private UI ui;
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TaskList() {
        this.tasks = new ArrayList<>();
        this.ui = new UI(); // For showing results
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
        this.ui = new UI(); // For showing results
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void addTask(Task task) throws BenException {
        tasks.add(task);
        saveToStorage();
    }

    public Task getTask(int index) throws BenException {
        validateIndex(index);
        return tasks.get(index - 1);
    }

    public Task deleteTask(int index) throws BenException {
        validateIndex(index);
        Task deletedTask = tasks.remove(index - 1);
        saveToStorage();
        return deletedTask;
    }

    public boolean mark(int index) throws BenException {
        validateIndex(index);
        this.tasks.get(index - 1).markComplete();
        saveToStorage();
        return true;
    }

    public boolean unmark(int index) throws BenException {
        validateIndex(index);
        this.tasks.get(index - 1).markIncomplete();
        saveToStorage();
        return true;
    }

    // Stretch goal: Show tasks due on a specific date
    public void showTasksDueOn(String dateString) throws BenException {
        LocalDate targetDate;
        try {
            targetDate = LocalDate.parse(dateString, INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new BenException("Invalid date format! Please use yyyy-mm-dd format (e.g., 2019-12-25)");
        }

        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task instanceof Deadline) {
                Deadline deadline = (Deadline) task;
                if (deadline.getDeadline().equals(targetDate)) {
                    matchingTasks.add(task);
                }
            }
        }

        if (matchingTasks.isEmpty()) {
            ui.showMessage("No deadlines found for " + targetDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy")));
        } else {
            StringBuilder result = new StringBuilder();
            result.append("Tasks due on ").append(targetDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))).append(":\n");
            for (int i = 0; i < matchingTasks.size(); i++) {
                result.append(" ").append(i + 1).append(".").append(matchingTasks.get(i)).append("\n");
            }
            ui.showMessage(result.toString().trim());
        }
    }

    private void validateIndex(int index) throws BenException {
        if (index < 1 || index > tasks.size()) {
            throw new BenException("Invalid ben.task number! Please choose a number between 1 and " + tasks.size() + ".");
        }
    }

    private void saveToStorage() throws BenException {
        if (storage != null) {
            storage.saveTasks(tasks);
        }
    }

    public int getSize() {
        return this.tasks.size();
    }

    @Override
    public String toString() {
        if (tasks.isEmpty()) {
            return "No tasks in your list.";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.getSize(); i++) {
            result.append(i + 1).append(".").append(tasks.get(i)).append("\n");
        }
        return result.toString().trim();
    }
}