import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class Deadline extends Task {
    private LocalDate by;
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    public Deadline(String description, String byString) throws BenException{
        super(description);
        this.by = parseDate(byString);
    }

    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    private LocalDate parseDate(String dateString) throws BenException {
        try {
            return LocalDate.parse(dateString, INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new BenException("Invalid date format! Please use yyyy-mm-dd format (e.g., 2019-12-25)");
        }
    }

    public LocalDate getBy() {
        return by;
    }

    public String getByString() {
        return by.format(OUTPUT_FORMAT);
    }

    @Override
    public String getType() {
        return "D";
    }

    @Override
    public String toString() {
        String status = isComplete() ? "[X]" : "[ ]";
        return "[" + getType() + "]" + status + " " + super.getDescription() + " (by: " + getByString() + ")";
    }
}
