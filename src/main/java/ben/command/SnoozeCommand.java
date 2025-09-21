package ben.command;

import ben.BenException;
import ben.storage.Storage;
import ben.task.*;
import ben.ui.UI;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Snooze command with flexible event time handling.
 * Supports multiple snoozing modes for events:
 * - Default: Move both start and end times (preserve duration)
 * - Start only: Change only start time, adjust end time to maintain duration
 * - Both separate: Specify both start and end times independently
 * - Duration change: Change start time and specify new duration
 */
public class SnoozeCommand extends Command {
    private String arguments;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    public SnoozeCommand(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(TaskList tasks, UI ui, Storage storage) throws BenException {
        if (arguments.trim().isEmpty()) {
            throw new BenException("Please specify which task to snooze! Format: snooze <task_number> <new_date> or snooze <task_number> +<days>");
        }

        String[] parts = arguments.trim().split("\\s+");
        if (parts.length < 2) {
            throw new BenException("Please specify both task number and new date/time!");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            throw new BenException("Task number must be a valid number!");
        }

        if (taskNumber < 1 || taskNumber > tasks.getSize()) {
            throw new BenException("Invalid task number! Please choose a number between 1 and " + tasks.getSize() + ".");
        }

        Task originalTask = tasks.getTask(taskNumber);

        if (!(originalTask instanceof Deadline) && !(originalTask instanceof Event)) {
            throw new BenException("Only deadline and event tasks can be snoozed! ToDo tasks don't have specific dates.");
        }

        Task snoozedTask = createSnoozedTask(originalTask, parts);

        tasks.deleteTask(taskNumber);
        tasks.addTask(snoozedTask);

        ui.showTaskSnoozed(originalTask, snoozedTask);
    }

    private Task createSnoozedTask(Task originalTask, String[] parts) throws BenException {
        if (originalTask instanceof Deadline) {
            return snoozeDeadline((Deadline) originalTask, parts[1]);
        } else if (originalTask instanceof Event) {
            return snoozeEvent((Event) originalTask, parts);
        }
        throw new BenException("Unsupported task type for snoozing.");
    }

    private Deadline snoozeDeadline(Deadline deadline, String newDateStr) throws BenException {
        LocalDate newDeadline = parseNewDate(newDateStr, deadline.getDeadline());

        Deadline snoozedDeadline = new Deadline(deadline.getDescription(), newDeadline);
        if (deadline.isComplete()) {
            snoozedDeadline.markComplete();
        }

        return snoozedDeadline;
    }

    private Event snoozeEvent(Event event, String[] parts) throws BenException {
        if (parts.length == 2) {
            return snoozeEventSimple(event, parts[1]);
        } else if (parts.length == 3) {
            return snoozeEventWithModifier(event, parts);
        } else if (parts.length == 4) {
            // Check for /start first, before other 4-argument formats
            if (parts[1].equals("/start")) {
                return snoozeEventWithModifier(event, parts);
            } else if (parts[1].equals("/from")) {
                return snoozeEventAdvanced(event, parts);
            } else {
                // Assume: date startTime endTime
                return snoozeEventAdvanced(event, parts);
            }
        } else if (parts.length >= 5) {
            return snoozeEventFull(event, parts);
        }

        throw new BenException("Invalid snooze format. Use 'help snooze' for examples.");
    }

    /**
     * Simple event snooze - moves both start and end times, preserving duration
     */
    private Event snoozeEventSimple(Event event, String timeStr) throws BenException {
        LocalDateTime originalStart = event.getStartTime();
        LocalDateTime originalEnd = event.getEndTime();
        long durationMinutes = java.time.Duration.between(originalStart, originalEnd).toMinutes();

        LocalDateTime newStartTime;

        if (timeStr.startsWith("+")) {
            int daysToAdd = parseRelativeDays(timeStr);
            newStartTime = originalStart.plusDays(daysToAdd);
        } else {
            try {
                if (timeStr.contains(" ")) {
                    newStartTime = LocalDateTime.parse(timeStr, DATETIME_FORMAT);
                } else {
                    LocalDate newDate = LocalDate.parse(timeStr, DATE_FORMAT);
                    newStartTime = newDate.atTime(originalStart.toLocalTime());
                }
            } catch (DateTimeParseException e) {
                throw new BenException("Invalid date format! Use yyyy-MM-dd or yyyy-MM-dd HHmm, or +<days>");
            }
        }

        LocalDateTime newEndTime = newStartTime.plusMinutes(durationMinutes);

        Event snoozedEvent = new Event(event.getDescription(), newStartTime, newEndTime);
        if (event.isComplete()) {
            snoozedEvent.markComplete();
        }

        return snoozedEvent;
    }

    /**
     * Event snooze with modifier - handles /start, /duration, or direct time specification
     */
    private Event snoozeEventWithModifier(Event event, String[] parts) throws BenException {
        String modifier = parts[1];

        if (modifier.equals("/start")) {
            // Check if we have time component too
            if (parts.length == 4) {
                // /start 2024-12-01 1300
                String fullDateTime = parts[2] + " " + parts[3];
                return changeEventStartTime(event, fullDateTime);
            } else {
                // /start 2024-12-01 (date only)
                return changeEventStartTime(event, parts[2]);
            }
        } else if (modifier.equals("/duration")) {
            // Change duration while keeping start time
            return changeEventDuration(event, parts[2]);
        } else {
            // Assume it's: snooze 1 2024-12-25 1400 (new start date and time)
            String fullDateTime = parts[1] + " " + parts[2];
            return snoozeEventSimple(event, fullDateTime);
        }
    }

    /**
     * Advanced event snooze - handles separate start/end times or /from modifier
     */
    private Event snoozeEventAdvanced(Event event, String[] parts) throws BenException {
        if (parts[1].equals("/from")) {
            // snooze 1 /from 2024-12-25 1400
            String fullDateTime = parts[2] + " " + parts[3];
            return changeEventStartTime(event, fullDateTime);
        } else {
            // Assume: snooze 1 2024-12-25 1400 1500 (date, start time, end time)
            try {
                String date = parts[1];
                String startTime = parts[2];
                String endTime = parts[3];

                LocalDateTime newStart = LocalDateTime.parse(date + " " + startTime, DATETIME_FORMAT);
                LocalDateTime newEnd = LocalDateTime.parse(date + " " + endTime, DATETIME_FORMAT);

                if (newEnd.isBefore(newStart) || newEnd.equals(newStart)) {
                    throw new BenException("End time must be after start time!");
                }

                Event snoozedEvent = new Event(event.getDescription(), newStart, newEnd);
                if (event.isComplete()) {
                    snoozedEvent.markComplete();
                }

                return snoozedEvent;
            } catch (DateTimeParseException e) {
                throw new BenException("Invalid date/time format! Use yyyy-MM-dd HHmm for start/end times");
            }
        }
    }

    /**
     * Full event snooze - handles /from ... /to ... format
     */
    private Event snoozeEventFull(Event event, String[] parts) throws BenException {
        if (!parts[1].equals("/from") || !parts[4].equals("/to")) {
            throw new BenException("Use format: snooze <num> /from <date time> /to <date time>");
        }

        try {
            String startDateTime = parts[2] + " " + parts[3];
            String[] toParts = java.util.Arrays.copyOfRange(parts, 5, parts.length);
            String endDateTime = String.join(" ", toParts);

            LocalDateTime newStart = LocalDateTime.parse(startDateTime, DATETIME_FORMAT);
            LocalDateTime newEnd = LocalDateTime.parse(endDateTime, DATETIME_FORMAT);

            if (newEnd.isBefore(newStart) || newEnd.equals(newStart)) {
                throw new BenException("End time must be after start time!");
            }

            Event snoozedEvent = new Event(event.getDescription(), newStart, newEnd);
            if (event.isComplete()) {
                snoozedEvent.markComplete();
            }

            return snoozedEvent;
        } catch (DateTimeParseException e) {
            throw new BenException("Invalid date/time format in /from or /to clause! Use yyyy-MM-dd HHmm");
        }
    }

    private Event changeEventStartTime(Event event, String newStartStr) throws BenException {
        long originalDuration = java.time.Duration.between(event.getStartTime(), event.getEndTime()).toMinutes();

        LocalDateTime newStart;
        try {
            if (newStartStr.contains(" ")) {
                newStart = LocalDateTime.parse(newStartStr, DATETIME_FORMAT);
            } else {
                LocalDate newDate = LocalDate.parse(newStartStr, DATE_FORMAT);
                newStart = newDate.atTime(event.getStartTime().toLocalTime());
            }
        } catch (DateTimeParseException e) {
            throw new BenException("Invalid start time format! Use yyyy-MM-dd or yyyy-MM-dd HHmm");
        }

        LocalDateTime newEnd = newStart.plusMinutes(originalDuration);

        Event snoozedEvent = new Event(event.getDescription(), newStart, newEnd);
        if (event.isComplete()) {
            snoozedEvent.markComplete();
        }

        return snoozedEvent;
    }

    private Event changeEventDuration(Event event, String durationStr) throws BenException {
        try {
            int newDurationMinutes = Integer.parseInt(durationStr);
            if (newDurationMinutes <= 0) {
                throw new BenException("Duration must be positive!");
            }

            LocalDateTime newEnd = event.getStartTime().plusMinutes(newDurationMinutes);

            Event snoozedEvent = new Event(event.getDescription(), event.getStartTime(), newEnd);
            if (event.isComplete()) {
                snoozedEvent.markComplete();
            }

            return snoozedEvent;
        } catch (NumberFormatException e) {
            throw new BenException("Duration must be a valid number (in minutes)!");
        }
    }

    private LocalDate parseNewDate(String dateStr, LocalDate originalDate) throws BenException {
        if (dateStr.startsWith("+")) {
            int daysToAdd = parseRelativeDays(dateStr);
            return originalDate.plusDays(daysToAdd);
        } else {
            try {
                return LocalDate.parse(dateStr, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                throw new BenException("Invalid date format! Use yyyy-MM-dd or +<days>");
            }
        }
    }

    private int parseRelativeDays(String relativeDayStr) throws BenException {
        if (!relativeDayStr.startsWith("+")) {
            throw new BenException("Relative days must start with '+' (e.g., +3 for 3 days later).");
        }

        try {
            int days = Integer.parseInt(relativeDayStr.substring(1));
            if (days <= 0) {
                throw new BenException("Number of days to snooze must be positive!");
            }
            return days;
        } catch (NumberFormatException e) {
            throw new BenException("Invalid number format for relative days! Use +<number>");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}