package ben;

import ben.command.Command;
import ben.parser.Parser;
import ben.storage.Storage;
import ben.task.TaskList;
import ben.ui.UI;
/**
 * Main application class for the Ben chatbot.
 * Manages the overall application flow, user interaction, and coordination
 * between UI, task management, and storage components.
 *
 * @author [Your Name]
 * @version 1.0
 */
public class Ben {
    private TaskList tasks;
    private UI ui;
    private Storage storage;
    private String name = "ben.Ben";
    private static final String FILE_PATH = "./data/ben.txt";

    /**
     * Initializes the Ben chatbot with UI, storage, and task list components.
     * Attempts to load existing tasks from storage, creates empty list if loading fails.
     */
    public Ben() {
        ui = new UI();
        storage = new Storage(FILE_PATH);
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (BenException e) {
            ui.showError("Problem loading tasks: " + e.getMessage());
            tasks = new TaskList();
        }
        tasks.setStorage(storage);
    }

    /**
     * Starts the main application loop.
     * Displays welcome message, processes user commands until exit command,
     * and handles exceptions gracefully with error messages.
     */
    public void run() {
        ui.showWelcome(name);

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine(); // show the divider line ("_______")
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (BenException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }

        ui.showGoodbye();
        ui.close();
    }

    /**
     * Entry point for the Ben chatbot application.
     * Creates a new Ben instance and starts the command processing loop.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new Ben().run();
    }
}