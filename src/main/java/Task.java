abstract class Task {
    private String description;
    private boolean complete;

    public Task(String description) {
        this.description = description;
        this.complete = false;
    }

    public boolean isComplete() {
        return complete;
    }

    public void markComplete() {
        this.complete = true;
    }

    public void markIncomplete() {
        this.complete = false;
    }

    public String getDescription() {
        return this.description;
    }

    public abstract String getType();

    @Override
    public String toString() {
        String status = isComplete() ? "[X]" : "[]";
        return "[" + getType() + "]" + status + " " + description;
    }
}
