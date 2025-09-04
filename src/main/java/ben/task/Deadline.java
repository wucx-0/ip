package ben.task;

import ben.BenException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {
    private LocalDate deadline;
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    public Deadline(String description, String byString) throws BenException {
        super(description);
        this.deadline = parseDate(byString);
    }

    public Deadline(String description, LocalDate deadline) {
        super(description);
        this.deadline = deadline;
    }

    private LocalDate parseDate(String dateString) throws BenException {
        try {
            return LocalDate.parse(dateString, INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new BenException("Invalid date format! Please use yyyy-mm-dd format (e.g., 2019-12-25)");
        }
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public String getByString() {
        return deadline.format(OUTPUT_FORMAT);
    }

    @Override
    public String getType() {
        return "D";
    }

    @Override
    public String toString() {
        String status = isComplete() ? "[X]" : "[ ]";
        return "[" + getType() + "]" + status + " " + super.getDescription() + " (deadline: " + getByString() + ")";
    }
}
