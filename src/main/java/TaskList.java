import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;
    private Storage storage;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
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

    private void validateIndex(int index) throws BenException {
        if (index < 1 || index > tasks.size()) {
            throw new BenException("Invalid task number! Please choose a number between 1 and " + tasks.size() + ".");
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