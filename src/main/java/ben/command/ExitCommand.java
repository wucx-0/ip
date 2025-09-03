package ben.command;

import ben.BenException;
import ben.storage.Storage;
import ben.task.TaskList;
import ben.ui.UI;

public class ExitCommand extends Command {

    @Override
    public void execute(TaskList tasks, UI ui, Storage storage) throws BenException {
        // Nothing to do - the program will exit based on isExit() returning true
    }

    @Override
    public boolean isExit() {
        return true;
    }
}