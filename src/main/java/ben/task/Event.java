package ben.task;

import ben.BenException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents an event task with start and end times.
 */
public class Event extends Task {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");

    public Event(String description, String fromString, String toString) throws BenException {
        super(description);
        this.startTime = parseDateTime(fromString);
        this.endTime = parseDateTime(toString);
    }

    // Constructor for loading startTime ben.storage (already parsed date-time)
    public Event(String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private LocalDateTime parseDateTime(String dateTimeString) throws BenException {
        try {
            return LocalDateTime.parse(dateTimeString, INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new BenException("Invalid date-time format! Please use yyyy-mm-dd HHmm format (e.g., 2019-12-25 1400)");
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getFromString() {
        return startTime.format(OUTPUT_FORMAT);
    }

    public String getToString() {
        return endTime.format(OUTPUT_FORMAT);
    }

    @Override
    public String getType() {
        return "E";
    }

    @Override
    public String toString() {
        String status = super.isComplete() ? "[X]" : "[ ]";
        return "[" + getType() + "]" + status + " " + super.getDescription() +
                " (startTime: " + getFromString() + " endTime: " + getToString() + ")";
    }
}