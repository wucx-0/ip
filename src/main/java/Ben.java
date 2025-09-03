public class Ben {
    private TaskList tasks;
    private UI ui;
    private Storage storage;
    private String name = "Ben";
    private static final String FILE_PATH = "./data/ben.txt";

    public Ben() {
        ui = new UI();
        storage = new Storage(FILE_PATH);
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (BenException e) {
            ui.showError("Problem loading tasks: " + e.getMessage());
            tasks = new TaskList();
        }
        tasks.setStorage(storage);
    }

    public void run() {
        ui.showWelcome(name);

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine(); // show the divider line ("_______")
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (BenException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }

        ui.showGoodbye();
        ui.close();
    }

    public static void main(String[] args) {
        new Ben().run();
    }
}