package ben.task;

/**
 * Represents a simple todo task without time constraints.
 */
public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String getType() {
        return "T";
    }
}