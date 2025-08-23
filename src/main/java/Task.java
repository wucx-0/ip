public class Task {
    private String task;
    private boolean complete;

    public Task(String task) {
        this.task = task;
        this.complete = false;
    }

    public boolean isComplete() {
        return complete;
    }

    public void changeCompletion() {
        this.complete = !this.complete;
    }

    @Override
    public String toString() {
        String status = isComplete() ? "[X]" : "[]";
        return status + " " + task;
    }
}
