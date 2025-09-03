public class DueCommand extends Command {
    private String arguments;

    public DueCommand(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(TaskList tasks, UI ui, Storage storage) throws BenException {
        if (arguments.trim().isEmpty()) {
            throw new BenException("Please specify a date! Format: due <yyyy-mm-dd>");
        }

        tasks.showTasksDueOn(arguments.trim());
    }

    @Override
    public boolean isExit() {
        return false;
    }
}