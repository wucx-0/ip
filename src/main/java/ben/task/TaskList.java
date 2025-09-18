package ben.task;

import ben.BenException;
import ben.storage.Storage;
import ben.ui.UI;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Manages a collection of tasks with operations for adding, removing,
 * marking, and searching tasks. Integrates with storage for data persistence.
 */
public class TaskList {
    private ArrayList<Task> tasks;
    private Storage storage;
    private UI ui;
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructs an empty task list.
     * Initializes the internal ArrayList and creates a UI instance for display operations.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        this.ui = new UI(); // For showing results
    }

    /**
     * Constructs a task list with existing tasks loaded from storage.
     *
     * @param tasks the list of tasks to initialize the task list with
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
        this.ui = new UI(); // For showing results
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    /**
     * Adds a new task to the list and automatically saves to storage.
     *
     * @param task the task to add to the list
     * @throws BenException if the task cannot be added or saved to storage
     */
    public void addTask(Task task) throws BenException {
        assert task != null : "Task to be added should not be null";
        assert task.getDescription() != null && !task.getDescription().trim().isEmpty() :
                "Task description should not be null or empty";

        int oldSize = tasks.size();
        tasks.add(task);

        assert tasks.size() == oldSize + 1 : "Task list size should increase by 1 after addition";
        assert tasks.get(tasks.size() - 1) == task : "Last task should be the one just added";

        saveToStorage();
    }

    /**
     * Retrieves a task at the specified 1-based index position.
     *
     * @param index the 1-based position of the task to retrieve (1 = first task)
     * @return the task at the specified position
     * @throws BenException if the index is less than 1 or greater than the list size
     */
    public Task getTask(int index) throws BenException {
        validateIndex(index);
        assert index >= 1 && index <= tasks.size() : "Index should be validated before reaching this point";
        assert tasks.get(index - 1) != null : "Task at valid index should never be null";
        return tasks.get(index - 1);
    }

    /**
     * Removes and returns the task at the specified index, then saves to storage.
     *
     * @param index the 1-based position of the task to delete
     * @return the deleted task
     * @throws BenException if the index is invalid or storage save fails
     */
    public Task deleteTask(int index) throws BenException {
        validateIndex(index);
        int oldSize = tasks.size();
        assert oldSize > 0 : "Cannot delete from empty task list";

        Task deletedTask = tasks.remove(index - 1);
        assert deletedTask != null : "Deleted task should not be null";
        assert tasks.size() == oldSize - 1 : "Task list size should decrease by 1 after deletion";

        saveToStorage();
        return deletedTask;
    }

    /**
     * Marks the task at the specified index as completed and saves to storage.
     *
     * @param index the 1-based position of the task to mark as done
     * @return true if the task was successfully marked as completed
     * @throws BenException if the index is invalid or storage save fails
     */
    public boolean mark(int index) throws BenException {
        validateIndex(index);
        Task task = this.tasks.get(index - 1);
        assert task != null : "Task at valid index should not be null";

        boolean wasComplete = task.isComplete();
        task.markComplete();

        assert task.isComplete() : "Task should be marked as complete after calling markComplete()";
        assert task.isComplete() != wasComplete || wasComplete : "Task completion status should change or was already complete";

        saveToStorage();
        return true;
    }

    /**
     * Marks the task at the specified index as not completed and saves to storage.
     *
     * @param index the 1-based position of the task to mark as not done
     * @return true if the task was successfully marked as not completed
     * @throws BenException if the index is invalid or storage save fails
     */
    public boolean unmark(int index) throws BenException {
        validateIndex(index);
        this.tasks.get(index - 1).markIncomplete();
        saveToStorage();
        return true;
    }

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

    /**
     * Searches for tasks containing the specified keyword and displays results.
     * Performs case-insensitive search on task descriptions.
     *
     * @param keyword the search term to look for in task descriptions
     * @param ui the UI component used to display search results
     */
    public void findTasksContaining(String keyword, UI ui) {
        ArrayList<Task> matchingTasks = new ArrayList<>();

        // Search through all tasks for the keyword (case-insensitive)
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.add(task);
            }
        }

        // Display results
        if (matchingTasks.isEmpty()) {
            ui.showMessage("No matching tasks found for: " + keyword);
        } else {
            StringBuilder result = new StringBuilder();
            result.append("Here are the matching tasks in your list:\n");
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

    /**
     * Returns the current number of tasks in the list.
     *
     * @return the total count of tasks currently stored
     */
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