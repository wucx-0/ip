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
        int oldSize = tasks.getSize();
        command.execute(tasks, null, storage);
        Task addedTask = tasks.getTask(tasks.getSize());
        return "Got it. I've added this task:\n   " + addedTask +
                "\nNow you have " + tasks.getSize() + " tasks in the list.";
    }

    private String executeDeleteCommand(Command command) throws BenException {
        // We need to capture the task before deletion
        String[] parts = command.getClass().getDeclaredFields()[0].getName().equals("arguments") ?
                new String[]{""} : new String[]{""};
        // This is a simplified approach - you might need to modify DeleteCommand
        // to return the deleted task info
        command.execute(tasks, null, storage);
        return "Noted. I've removed the task.\nNow you have " + tasks.getSize() + " tasks in the list.";
    }

    private String executeMarkCommand(Command command) throws BenException {
        command.execute(tasks, null, storage);
        return "Nice! I've updated the task status.";
    }

    private String executeFindCommand(Command command) throws BenException {
        // Capture the output from find command
        StringBuilder result = new StringBuilder();
        command.execute(tasks, new UI() {
            @Override
            public void showMessage(String message) {
                result.append(message);
            }
        }, storage);
        return result.toString();
    }

    private String executeDueCommand(Command command) throws BenException {
        StringBuilder result = new StringBuilder();
        command.execute(tasks, new UI() {
            @Override
            public void showMessage(String message) {
                result.append(message);
            }
        }, storage);
        return result.toString();
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