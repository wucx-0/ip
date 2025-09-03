package ben.command;

import ben.BenException;
import ben.storage.Storage;
import ben.task.TaskList;
import ben.ui.UI;

public class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, UI ui, Storage storage) throws BenException {
        ui.showTaskList(tasks);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}