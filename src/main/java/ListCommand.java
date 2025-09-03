
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