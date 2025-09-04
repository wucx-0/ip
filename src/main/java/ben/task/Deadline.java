package ben.task;

import ben.BenException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a task with a specific deadline date and time.
 */
public class Deadline extends Task {
    private LocalDate deadline;
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    /**
     * Constructs a deadline task with description and due date string.
     * Parses the date string into a LocalDate object for internal storage.
     *
     * @param description the task description
     * @param deadlineString the due date in yyyy-MM-dd format
     * @throws BenException if the date format is invalid or cannot be parsed
     */
    public Deadline(String description, String deadlineString) throws BenException {
        super(description);
        this.deadline = parseDate(deadlineString);
    }

    /**
     * Constructs a deadline task with description and pre-parsed due date.
     * Used when loading from storage where date is already parsed.
     *
     * @param description the task description
     * @param deadline the due date as a LocalDate object
     */
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

    /**
     * Gets the due date of this deadline task.
     *
     * @return the due date as a LocalDate object
     */
    public LocalDate getDeadline() {
        return deadline;
    }

    /**
     * Gets the due date formatted for display purposes.
     *
     * @return the due date in "MMM dd yyyy" format (e.g., "Dec 25 2019")
     */
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
