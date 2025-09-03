package ben.task;

import ben.BenException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Event extends Task {
    private LocalDateTime from;
    private LocalDateTime to;
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");

    public Event(String description, String fromString, String toString) throws BenException {
        super(description);
        this.from = parseDateTime(fromString);
        this.to = parseDateTime(toString);
    }

    // Constructor for loading from ben.storage (already parsed date-time)
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    private LocalDateTime parseDateTime(String dateTimeString) throws BenException {
        try {
            return LocalDateTime.parse(dateTimeString, INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new BenException("Invalid date-time format! Please use yyyy-mm-dd HHmm format (e.g., 2019-12-25 1400)");
        }
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public String getFromString() {
        return from.format(OUTPUT_FORMAT);
    }

    public String getToString() {
        return to.format(OUTPUT_FORMAT);
    }

    @Override
    public String getType() {
        return "E";
    }

    @Override
    public String toString() {
        String status = super.isComplete() ? "[X]" : "[ ]";
        return "[" + getType() + "]" + status + " " + super.getDescription() +
                " (from: " + getFromString() + " to: " + getToString() + ")";
    }
}