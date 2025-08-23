import java.util.Scanner;

public class Ben {
    private TaskList tasks;
    private UI ui;
    private String name = "Ben";

    public Ben() {
        this.ui = new UI();
        this.tasks = new TaskList();
    }

    public void run() {
        ui.showWelcome(name);

        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            isExit = executeCommand(fullCommand);
        }

        ui.showGoodbye();
        ui.close();
    }

    public boolean executeCommand(String fullCommand) {
        if (fullCommand.equals("bye")) {
            return true; // Signal to exit
        } else if (fullCommand.equals("list")) {
            ui.showTaskList(tasks);
        } else if (fullCommand.startsWith("mark ")) {
            handleMarkCommand(fullCommand);

        } else if (fullCommand.startsWith("unmark ")) {
            handleUnmarkCommand(fullCommand);
        } else {
            tasks.addTask(fullCommand);
            ui.showMessage("added: " + fullCommand);
        }
        return false;
    }

    private void handleMarkCommand(String command) {
        try {
            String[] parts = command.split(" ");
            if (parts.length != 2) {
                ui.showInvalidMarkCommand();
                return;
            }
            int taskNumber = Integer.parseInt(parts[1]);
            boolean success = tasks.mark(taskNumber);

            if (success) {
                Task task = tasks.getTask(taskNumber - 1);
                ui.showTaskMarkedDone(task);
            } else {
                ui.showInvalidTaskNumber();
            }
        } catch (NumberFormatException e) {
            ui.showInvalidMarkCommand();
        }
    }

    private void handleUnmarkCommand(String command) {
        try {
            String[] parts = command.split(" ");
            if (parts.length != 2) {
                ui.showInvalidMarkCommand();
                return;
            }
            int taskNumber = Integer.parseInt(parts[1]);
            boolean success = tasks.unmark(taskNumber);

            if (success) {
                Task task = tasks.getTask(taskNumber - 1);
                ui.showTaskMarkedNotDone(task);
            } else {
                ui.showInvalidTaskNumber();
            }
        } catch (NumberFormatException e) {
            ui.showInvalidMarkCommand();
        }
    }

    public static void main(String[] args) {
        new Ben().run();
    }
}
