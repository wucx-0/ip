package ben;

import ben.command.Command;
import ben.parser.Parser;
import ben.storage.Storage;
import ben.task.Task;
import ben.task.TaskList;
import ben.ui.UI;

/**
 * Main application class for the Ben chatbot.
 * Supports both CLI and GUI modes.
 *
 * @author [Your Name]
 * @version 2.0
 */
public class Ben {
    private TaskList tasks;
    private UI ui;
    private Storage storage;
    private String name = "Ben";
    private static final String FILE_PATH = "./data/ben.txt";

    /**
     * Initializes the Ben chatbot with UI, storage, and task list components.
     * Attempts to load existing tasks from storage, creates empty list if loading fails.
     */
    public Ben() {
        ui = new UI();
        storage = new Storage(FILE_PATH);

        assert ui != null : "UI should be initialized";
        assert storage != null : "Storage should be initialized";
        assert FILE_PATH != null && !FILE_PATH.trim().isEmpty() : "File path should be valid";

        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (BenException e) {
            ui.showError("Problem loading tasks: " + e.getMessage());
            tasks = new TaskList();
        }

        assert tasks != null : "TaskList should always be initialized";

        tasks.setStorage(storage);

        // Verify the wiring is correct
        assert tasks != null && ui != null && storage != null :
                "All core components should be initialized";
    }

    /**
     * Starts the main application loop for CLI mode.
     * Displays welcome message, processes user commands until exit command,
     * and handles exceptions gracefully with error messages.
     */
    public void run() {
        runWithUI(ui);
    }

    /**
     * Runs the application with a specific UI implementation.
     * This method contains the core application logic separated from UI concerns.
     *
     * @param userInterface the UI implementation to use for interaction
     */
    public void runWithUI(UI userInterface) {
        userInterface.showWelcome(name);

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = userInterface.readCommand();
                userInterface.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, userInterface, storage);
                isExit = c.isExit();
            } catch (BenException e) {
                userInterface.showError(e.getMessage());
            } finally {
                userInterface.showLine();
            }
        }

        userInterface.showGoodbye();
        userInterface.close();
    }

    /**
     * Processes a single command and returns the result.
     * This method is used by GUI implementations that don't need the full CLI loop.
     *
     * @param input the command string to process
     * @return the response message from executing the command
     */
    public String processCommand(String input) {
        assert input != null : "Input command should not be null";
        try {
            Command c = Parser.parse(input);

            // For commands that need special handling in non-CLI mode
            if (c.isExit()) {
                return "Bye. Hope to see you again soon!";
            }

            // Create a mock UI to capture output
            StringBuilder output = new StringBuilder();
            UI mockUI = new UI() {
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

            c.execute(tasks, mockUI, storage);
            return output.toString();

        } catch (BenException e) {
            return "OOPS!!! " + e.getMessage();
        }
    }

    /**
     * Entry point for the Ben chatbot application.
     * Supports both CLI and GUI modes via command line arguments.
     *
     * @param args command line arguments:
     *             - no args or "cli": runs in CLI mode
     *             - "gui": runs in GUI mode
     */
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
            // Launch GUI mode
            Launcher.main(args);
        } else {
            // Default CLI mode
            new Ben().run();
        }
    }
}