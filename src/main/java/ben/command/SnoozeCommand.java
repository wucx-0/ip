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
 * Command to snooze/postpone tasks to a later date or time.
 * Supports snoozing deadlines and events, with flexible date/time formats.
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

        String[] parts = arguments.trim().split("\\s+", 2);
        if (parts.length < 2) {
            throw new BenException("Please specify both task number and new date! Format: snooze <task_number> <new_date> or snooze <task_number> +<days>");
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
        String newDateTimeStr = parts[1];

        // Check if task can be snoozed
        if (!(originalTask instanceof Deadline) && !(originalTask instanceof Event)) {
            throw new BenException("Only deadline and event tasks can be snoozed! ToDo tasks don't have specific dates.");
        }

        Task snoozedTask = createSnoozedTask(originalTask, newDateTimeStr);

        // Replace the original task with the snoozed task
        tasks.deleteTask(taskNumber);
        tasks.addTask(snoozedTask);

        ui.showTaskSnoozed(originalTask, snoozedTask);
    }

    private Task createSnoozedTask(Task originalTask, String newDateTimeStr) throws BenException {
        if (originalTask instanceof Deadline) {
            return snoozeDeadline((Deadline) originalTask, newDateTimeStr);
        } else if (originalTask instanceof Event) {
            return snoozeEvent((Event) originalTask, newDateTimeStr);
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

    private Event snoozeEvent(Event event, String newDateTimeStr) throws BenException {
        LocalDateTime originalStart = event.getStartTime();
        LocalDateTime originalEnd = event.getEndTime();
        long durationMinutes = java.time.Duration.between(originalStart, originalEnd).toMinutes();

        LocalDateTime newStartTime;

        if (newDateTimeStr.startsWith("+")) {
            int daysToAdd = parseRelativeDays(newDateTimeStr);
            newStartTime = originalStart.plusDays(daysToAdd);
        } else {
            try {
                if (newDateTimeStr.contains(" ")) {
                    newStartTime = LocalDateTime.parse(newDateTimeStr, DATETIME_FORMAT);
                } else {
                    LocalDate newDate = LocalDate.parse(newDateTimeStr, DATE_FORMAT);
                    newStartTime = newDate.atTime(originalStart.toLocalTime());
                }
            } catch (DateTimeParseException e) {
                throw new BenException("Invalid date format! Use yyyy-MM-dd or yyyy-MM-dd HHmm for events, or +<days> for relative snoozing.");
            }
        }

        LocalDateTime newEndTime = newStartTime.plusMinutes(durationMinutes);

        Event snoozedEvent = new Event(event.getDescription(), newStartTime, newEndTime);
        if (event.isComplete()) {
            snoozedEvent.markComplete();
        }

        return snoozedEvent;
    }

    private LocalDate parseNewDate(String dateStr, LocalDate originalDate) throws BenException {
        if (dateStr.startsWith("+")) {
            int daysToAdd = parseRelativeDays(dateStr);
            return originalDate.plusDays(daysToAdd);
        } else {
            try {
                return LocalDate.parse(dateStr, DATE_FORMAT);
            } catch (DateTimeParseException e) {
                throw new BenException("Invalid date format! Use yyyy-MM-dd or +<days> for relative snoozing (e.g., +3 for 3 days later).");
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
            throw new BenException("Invalid number format for relative days! Use +<number> (e.g., +3 for 3 days later).");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}