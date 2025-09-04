package ben.command;

import ben.BenException;
import ben.storage.Storage;
import ben.task.TaskList;
import ben.ui.UI;

public class FindCommand extends Command {
    private String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword.trim();
    }

    @Override
    public void execute(TaskList tasks, UI ui, Storage storage) throws BenException {
        if (keyword.isEmpty()) {
            throw new BenException("Please specify a keyword to search for! Format: find <keyword>");
        }

        tasks.findTasksContaining(keyword, ui);
    }

    @Override
    public boolean isExit() {
        return false;
    }
}
