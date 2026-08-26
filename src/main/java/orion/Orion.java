package orion;

import orion.exception.OrionException;
import orion.parser.Parser;
import orion.storage.Storage;
import orion.task.TaskList;
import orion.ui.Ui;

public class Orion {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Orion(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (OrionException e) {
            ui.showError(e.getMessage() + "\nInitialising an empty task list...");
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isRunning = true;
        while (isRunning) {
            String command = ui.readCommand();
            isRunning = Parser.parseAndExecute(command, tasks, ui, storage);
        }
    }

    public static void main(String[] args) {
        new Orion("./data/orion.txt").run();
    }
}
