public class AddCommand extends Command {
    private String taskType;
    private String arguments;

    public AddCommand(String taskType, String arguments) {
        this.taskType = taskType;
        this.arguments = arguments;
    }

    @Override
    public void execute(TaskList tasks, UI ui, Storage storage) throws BenException {
        Task task;

        switch (taskType.toLowerCase()) {
            case "todo":
                task = createTodoTask(arguments);
                break;

            case "deadline":
                task = createDeadlineTask(arguments);
                break;

            case "event":
                task = createEventTask(arguments);
                break;

            default:
                throw new BenException("Unknown task type: " + taskType);
        }

        tasks.addTask(task);
        ui.showTaskAdded(task, tasks.getSize());
    }

    private Task createTodoTask(String description) throws BenException {
        if (description.trim().isEmpty()) {
            throw new BenException("The description of a todo cannot be empty.");
        }
        return new ToDo(description.trim());
    }

    private Task createDeadlineTask(String content) throws BenException {
        if (content.trim().isEmpty()) {
            throw new BenException("The description of a deadline cannot be empty.");
        }

        String[] parts = content.split(" /by ", 2);
        if (parts.length != 2) {
            throw new BenException("Please use format: deadline <description> /by <yyyy-mm-dd>");
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty()) {
            throw new BenException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new BenException("The deadline date cannot be empty.");
        }

        return new Deadline(description, by);
    }

    private Task createEventTask(String content) throws BenException {
        if (content.trim().isEmpty()) {
            throw new BenException("The description of an event cannot be empty.");
        }

        String[] fromSplit = content.split(" /from ", 2);
        if (fromSplit.length != 2) {
            throw new BenException("Please use format: event <description> /from <yyyy-mm-dd HHmm> /to <yyyy-mm-dd HHmm>");
        }

        String description = fromSplit[0].trim();
        String[] toSplit = fromSplit[1].split(" /to ", 2);
        if (toSplit.length != 2) {
            throw new BenException("Please use format: event <description> /from <yyyy-mm-dd HHmm> /to <yyyy-mm-dd HHmm>");
        }

        String from = toSplit[0].trim();
        String to = toSplit[1].trim();

        if (description.isEmpty()) {
            throw new BenException("The description of an event cannot be empty.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new BenException("The event date-time cannot be empty.");
        }

        return new Event(description, from, to);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}