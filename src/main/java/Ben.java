import java.util.Scanner;

public class Ben {
    private TaskList tasks;
    private UI ui;
    private Storage storage;
    private String name = "Ben";
    private static final String FILE_PATH = "./data/ben.txt";

    public Ben() {
        this.ui = new UI();
        this.storage = new Storage(FILE_PATH);
        try {
            this.tasks = new TaskList(storage.loadTasks());
        } catch (BenException e) {
            ui.showError("Problem loading tasks: " + e.getMessage());
            this.tasks = new TaskList();
        }
        this.tasks.setStorage(storage);
    }

    public void run() throws BenException{
        ui.showWelcome(name);

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                isExit = executeCommand(fullCommand);
            } catch (BenException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.showGoodbye();
        ui.close();
    }

    public boolean executeCommand(String fullCommand) throws BenException {
        String command = fullCommand.trim();
        if (command.equals("bye")) {
            return true; // Signal to exit
        } else if (command.equals("list")) {
            ui.showTaskList(tasks);
        } else if (command.startsWith("mark ")) {
            handleMarkCommand(command);

        } else if (command.startsWith("unmark ")) {
            handleUnmarkCommand(command);
        } else if (command.startsWith("delete ")) {
            handleDeleteCommand(command);
        } else if (command.startsWith("todo ")) {
            handleTodoCommand(command);
        } else if (command.startsWith("deadline ")) {
            handleDeadlineCommand(command);
        } else if (command.startsWith("event ")) {
            handleEventCommand(command);
        } else if (command.equals("todo") || command.equals("deadline") || command.equals("event")) {
            throw new BenException("The description cannot be empty! Please specify what you want to " + command + ".");
        } else if (command.isEmpty()) {
            throw new BenException("Enter a command!");
        } else {
            throw new BenException("I'm sorry, say that again?");
        }
        return false;
    }

    private void handleTodoCommand(String command) throws BenException {
        String description = command.substring(5).trim();
        if (description.isEmpty()) {
            throw new BenException("The description of a todo cannot be empty.");
        }

        Task todo = new ToDo(description);
        tasks.addTask(todo);
        ui.showTaskAdded(todo, tasks.getSize());
    }

    private void handleDeadlineCommand(String command) throws BenException {
        String content = command.substring(9).trim();
        if (content.isEmpty()) {
            throw new BenException("The description of a deadline cannot be empty.");
        }

        String[] parts = content.split(" /by ", 2);
        if (parts.length != 2) {
            throw new BenException("Please use format: deadline <description> /by <time>");
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty()) {
            throw new BenException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new BenException("The deadline time cannot be empty.");
        }

        Task deadline = new Deadline(description, by);
        tasks.addTask(deadline);
        ui.showTaskAdded(deadline, tasks.getSize());
    }

    private void handleEventCommand(String command) throws BenException {
        String content = command.substring(6).trim();
        if (content.isEmpty()) {
            throw new BenException("The description of an event cannot be empty.");
        }

        String[] fromSplit = content.split(" /from ", 2);
        if (fromSplit.length != 2) {
            throw new BenException("Please use format: event <description> /from <start> /to <end>");
        }

        String description = fromSplit[0].trim();
        String[] toSplit = fromSplit[1].split(" /to ", 2);
        if (toSplit.length != 2) {
            throw new BenException("Please use format: event <description> /from <start> /to <end>");
        }

        String from = toSplit[0].trim();
        String to = toSplit[1].trim();

        if (description.isEmpty()) {
            throw new BenException("The description of an event cannot be empty.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new BenException("The event time cannot be empty.");
        }

        Task event = new Event(description, from, to);
        tasks.addTask(event);
        ui.showTaskAdded(event, tasks.getSize());
    }

    private void handleMarkCommand(String command) throws BenException {
        int taskNumber = parseTaskNumber(command, "mark");
        tasks.mark(taskNumber);
        Task task = tasks.getTask(taskNumber);
        ui.showTaskMarkedDone(task);
    }

    private void handleUnmarkCommand(String command) throws BenException {
        int taskNumber = parseTaskNumber(command, "unmark");
        tasks.unmark(taskNumber);
        Task task = tasks.getTask(taskNumber);
        ui.showTaskMarkedNotDone(task);
    }

    private void handleDeleteCommand(String command) throws BenException {
        int taskNumber = parseTaskNumber(command, "delete");
        Task deletedTask = tasks.deleteTask(taskNumber);
        ui.showTaskDeleted(deletedTask, tasks.getSize());
    }

    private int parseTaskNumber(String command, String commandType) throws BenException {
        String[] parts = command.split(" ");
        if (parts.length != 2) {
            throw new BenException("Please specify which task to " + commandType + " (e.g., '" + commandType + " 2').");
        }

        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new BenException("Task number must be a valid number!");
        }
    }

    public static void main(String[] args) throws BenException {
        new Ben().run();
    }
}