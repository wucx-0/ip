package ben.command;

import ben.BenException;
import ben.storage.Storage;
import ben.task.TaskList;
import ben.ui.UI;

public abstract class Command {

    public abstract void execute(TaskList tasks, UI ui, Storage storage) throws BenException;

    public abstract boolean isExit();

}
