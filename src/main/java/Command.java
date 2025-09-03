
public abstract class Command {

    public abstract void execute(TaskList tasks, UI ui, Storage storage) throws BenException;

    public abstract boolean isExit();

}
