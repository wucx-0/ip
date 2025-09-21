package ben;

import ben.command.Command;
import ben.parser.Parser;
import ben.storage.Storage;
import ben.task.TaskList;
import ben.ui.UI;

/**
 * Main application class for the Ben chatbot.
 * Handles CLI mode only - GUI mode is handled by separate GUI class.
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