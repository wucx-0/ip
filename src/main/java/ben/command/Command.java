package ben.command;

import ben.BenException;
import ben.storage.Storage;
import ben.task.TaskList;
import ben.ui.UI;

/**
 * Abstract base class for all executable commands in the Ben chatbot.
 * Implements the Command pattern to encapsulate user actions.
 */
public abstract class Command {

    /**
     * Executes the command with the provided application context.
     * Each concrete command implements this method to perform its specific action.
     *
     * @param tasks the task list to operate on
     * @param ui the user interface for displaying results and getting input
     * @param storage the storage system for persisting changes
     * @throws BenException if command execution fails due to invalid input or system errors
     */
    public abstract void execute(TaskList tasks, UI ui, Storage storage) throws BenException;

    /**
     * Determines whether this command should cause the application to terminate.
     *
     * @return true if this command should exit the application, false otherwise
     */
    public abstract boolean isExit();

}
