package ben.task;

/**
 * Abstract base class representing a task with description and completion status.
 */
public abstract class Task {
    private String description;
    private boolean isComplete;

    /**
     * Constructs a new task with the specified description.
     * The task is initially marked as not completed.
     *
     * @param description the description text for this task
     */
    public Task(String description) {
        this.description = description;
        this.isComplete = false;
    }

    /**
     * Checks the completion status of this task.
     *
     * @return true if the task has been marked as completed, false otherwise
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Marks this task as completed.
     */
    public void markComplete() {
        this.isComplete = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markIncomplete() {
        this.isComplete = false;
    }

    /**
     * Gets the description text of this task.
     *
     * @return the task's description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets the single-character type identifier for this task.
     * Each subclass provides its own implementation.
     *
     * @return the type identifier ("T" for ToDo, "D" for Deadline, "E" for Event)
     */
    public abstract String getType();

    @Override
    public String toString() {
        String status = isComplete() ? "[X]" : "[]";
        return "[" + getType() + "]" + status + " " + description;
    }
}
