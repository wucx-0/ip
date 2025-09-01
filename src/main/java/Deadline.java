class Deadline extends Task {
    private String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getType() {
        return "D";
    }

    public String getDeadline() {
        return this.by;
    }

    @Override
    public String toString() {
        String status = isComplete() ? "[X]" : "[ ]";
        return "[" + getType() + "]" + status + " " + super.getDescription() + " (by: " + by + ")";
    }
}
