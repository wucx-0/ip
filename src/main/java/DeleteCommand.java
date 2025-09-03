public class DeleteCommand extends Command {
    private String arguments;

    public DeleteCommand(String arguments) {
        this.arguments = arguments;
    }

    @Override
    public void execute(TaskList tasks, UI ui, Storage storage) throws BenException {
        int taskNumber = Parser.parseTaskNumber(arguments, "delete");
        Task deletedTask = tasks.deleteTask(taskNumber);
        ui.showTaskDeleted(deletedTask, tasks.getSize());
    }

    @Override
    public boolean isExit() {
        return false;
    }
}