package ben.command;

import ben.task.*;
import ben.parser.Parser;
import ben.ui.UI;
import ben.BenException;
import ben.storage.Storage;

public class MarkCommand extends Command {
    private String arguments;
    private boolean isMarking; // true for mark, false for unmark

    public MarkCommand(String arguments, boolean isMarking) {
        this.arguments = arguments;
        this.isMarking = isMarking;
    }

    @Override
    public void execute(TaskList tasks, UI ui, Storage storage) throws BenException {
        String commandType = isMarking ? "mark" : "unmark";
        int taskNumber = Parser.parseTaskNumber(arguments, commandType);

        if (isMarking) {
            tasks.mark(taskNumber);
            Task task = tasks.getTask(taskNumber);
            ui.showTaskMarkedDone(task);
        } else {
            tasks.unmark(taskNumber);
            Task task = tasks.getTask(taskNumber);
            ui.showTaskMarkedNotDone(task);
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }
}