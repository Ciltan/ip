package orion;

import orion.exception.OrionException;
import orion.parser.Parser;
import orion.storage.Storage;
import orion.task.TaskList;
import orion.ui.Ui;

/**
 * Serves as the main entry point for the Orion chatbot application.
 * It initializes the core components such as the user interface, storage,
 * and task list, and ties them together to run the program.
 */
public class Orion {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Initializes the Orion chatbot with the specified storage file path.
     * It attempts to load existing tasks from the file, or creates an empty task list
     * if the file cannot be loaded.
     *
     * @param filePath The relative path to the file where tasks are saved.
     */
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

    /**
     * Starts the main execution loop of the chatbot.
     * It displays the welcome message and continuously reads, parses,
     * and executes user commands until the exit command is given.
     */
    public void run() {
        ui.showWelcome();
        boolean isRunning = true;

        while (isRunning) {
            String command = ui.readCommand();
            isRunning = Parser.parseAndExecute(command, tasks, ui, storage);
        }
    }

    /**
     * Serves as the main entry point for the Java runtime environment.
     * Creates a new instance of the Orion chatbot and starts its execution.
     *
     * @param args Command line arguments supplied by the operating system.
     */
    public static void main(String[] args) {
        new Orion("./data/orion.txt").run();
    }
}
