public class TaskList {
    private Task[] tasks;
    private int size;
    private static final int MAX_TASKS = 100;

    public TaskList() {
        this.tasks = new Task[MAX_TASKS];
        this.size = 0;
    }

    public void addTask(String task) {
        if (this.size < MAX_TASKS) {
            this.tasks[size] = new Task(task);
            this.size++;
        }
    }

    public Task getTask(int index) {
        if (index >= 0 && index < size) {
            return this.tasks[index];
        }
        return null;
    }

    public int getSize() {
        return this.size;
    }

    public boolean mark(int index) {
        if (index >= 1 && index < size + 1) {
            this.tasks[index - 1].markComplete();
            return true;
        }
        return false;
    }

    public boolean unmark(int index) {
        if (index >= 1 && index < size + 1) {
            this.tasks[index - 1].markIncomplete();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "No tasks in your list.";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append(i + 1).append(".").append(tasks[i]).append("\n");
        }
        return result.toString().trim();
    }
}
