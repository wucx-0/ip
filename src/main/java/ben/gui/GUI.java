package ben.gui;

import ben.command.Command;
import ben.parser.Parser;
import ben.BenException;
import ben.storage.Storage;
import ben.task.TaskList;
import ben.task.Task;
import ben.ui.UI;

/**
 * GUI interface for Ben chatbot that handles command processing
 * and returns formatted responses for the GUI.
 */
public class GUI {
    private TaskList tasks;
    private Storage storage;
    private static final String FILE_PATH = "./data/ben.txt";

    /**
     * Initializes the Ben GUI backend with storage and task list components.
     */
    public GUI() {
        storage = new Storage(FILE_PATH);
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (BenException e) {
            tasks = new TaskList();
        }
        tasks.setStorage(storage);
    }

    /**
     * Processes user input and returns the response as a string.
     *
     * @param input the user's command input
     * @return formatted response string for display in GUI
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parse(input);
            return executeCommand(c);
        } catch (BenException e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    private String executeCommand(Command command) throws BenException {
        if (command.getClass().getSimpleName().equals("ExitCommand")) {
            return "Bye. Hope to see you again soon!";
        } else if (command.getClass().getSimpleName().equals("ListCommand")) {
            return formatTaskList();
        } else if (command.getClass().getSimpleName().equals("AddCommand")) {
            return executeAddCommand(command);
        } else if (command.getClass().getSimpleName().equals("DeleteCommand")) {
            return executeDeleteCommand(command);
        } else if (command.getClass().getSimpleName().equals("MarkCommand")) {
            return executeMarkCommand(command);
        } else if (command.getClass().getSimpleName().equals("FindCommand")) {
            return executeFindCommand(command);
        } else if (command.getClass().getSimpleName().equals("DueCommand")) {
            return executeDueCommand(command);
        }
        return "Command executed successfully.";
    }

    private String executeAddCommand(Command command) throws BenException {
        // Create a mock UI to capture the output
        StringBuilder result = new StringBuilder();
        UI mockUI = createMockUI(result);

        command.execute(tasks, mockUI, storage);

        // If no output was captured, format it manually
        if (result.length() == 0) {
            Task addedTask = tasks.getTask(tasks.getSize());
            return "Got it. I've added this task:\n   " + addedTask +
                    "\nNow you have " + tasks.getSize() + " tasks in the list.";
        }
        return result.toString();
    }

    private String executeDeleteCommand(Command command) throws BenException {
        StringBuilder result = new StringBuilder();
        UI mockUI = createMockUI(result);

        command.execute(tasks, mockUI, storage);

        if (result.length() == 0) {
            return "Noted. I've removed the task.\nNow you have " + tasks.getSize() + " tasks in the list.";
        }
        return result.toString();
    }

    private String executeMarkCommand(Command command) throws BenException {
        StringBuilder result = new StringBuilder();
        UI mockUI = createMockUI(result);

        command.execute(tasks, mockUI, storage);

        if (result.length() == 0) {
            return "Task status updated successfully.";
        }
        return result.toString();
    }

    private String executeFindCommand(Command command) throws BenException {
        StringBuilder result = new StringBuilder();
        UI mockUI = createMockUI(result);

        command.execute(tasks, mockUI, storage);
        return result.toString();
    }

    private String executeDueCommand(Command command) throws BenException {
        StringBuilder result = new StringBuilder();
        UI mockUI = createMockUI(result);

        command.execute(tasks, mockUI, storage);
        return result.toString();
    }

    /**
     * Creates a mock UI that captures output into a StringBuilder
     */
    private UI createMockUI(StringBuilder output) {
        return new UI() {
            @Override
            public void showMessage(String message) {
                output.append(message);
            }

            @Override
            public void showTaskList(TaskList tasks) {
                output.append(tasks.toString());
            }

            @Override
            public void showTaskAdded(Task task, int totalTasks) {
                output.append("Got it. I've added this task:\n   ")
                        .append(task)
                        .append("\nNow you have ")
                        .append(totalTasks)
                        .append(" tasks in the list.");
            }

            @Override
            public void showTaskDeleted(Task task, int remainingTasks) {
                output.append("Noted. I've removed this task:\n   ")
                        .append(task)
                        .append("\nNow you have ")
                        .append(remainingTasks)
                        .append(" tasks in the list.");
            }

            @Override
            public void showTaskMarkedDone(Task task) {
                output.append("Nice! I've marked this task as done:\n   ")
                        .append(task);
            }

            @Override
            public void showTaskMarkedNotDone(Task task) {
                output.append("OK, I've marked this task as not done yet:\n   ")
                        .append(task);
            }

            @Override
            public void showError(String errorMessage) {
                output.append("OOPS!!! ").append(errorMessage);
            }

            // Unused methods for mock UI
            @Override
            public void showWelcome(String name) {}
            @Override
            public void showGoodbye() {}
            @Override
            public void showLine() {}
            @Override
            public String readCommand() { return ""; }
            @Override
            public void close() {}
        };
    }

    private String formatTaskList() {
        if (tasks.getSize() == 0) {
            return "No tasks in your list.";
        }

        StringBuilder result = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 1; i <= tasks.getSize(); i++) {
            try {
                Task task = tasks.getTask(i);
                result.append(i).append(".").append(task).append("\n");
            } catch (BenException e) {
                // Skip invalid tasks
            }
        }
        return result.toString().trim();
    }
}