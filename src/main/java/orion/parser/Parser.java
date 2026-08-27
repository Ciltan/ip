package orion.parser;

import orion.exception.OrionException;
import orion.storage.Storage;
import orion.task.Deadline;
import orion.task.Event;
import orion.task.Task;
import orion.task.TaskList;
import orion.task.Todo;
import orion.ui.Ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Handles the parsing of user input and execution of commands.
 */
public class Parser {

    /**
     * Parses the user input and executes the corresponding command.
     *
     * @param input Raw command string entered by the user.
     * @param tasks TaskList containing the current tasks.
     * @param ui Ui object to handle user interactions.
     * @param storage Storage object to handle saving data.
     * @return {@code true} if the program should continue running, {@code false} if it should exit.
     */
    public static boolean parseAndExecute(String input, TaskList tasks, Ui ui, Storage storage) {
        try {
            String[] parts = input.split(" ", 2);
            Command command;
            try {
                command = Command.valueOf(parts[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new OrionException("That is not a valid command!");
            }

            String arguments = parts.length == 2 ? parts[1] : null;

            switch (command) {
                case LIST:
                    ui.showLine();
                    ui.showMessage("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.getSize(); i++) {
                        ui.showMessage((i + 1) + ". " + tasks.getTask(i));
                    }
                    ui.showLine();
                    break;

                case BYE:
                    ui.showGoodbye();
                    return false;

                case MARK:
                    markTask(Integer.parseInt(arguments) - 1, true, tasks, ui, storage);
                    break;

                case UNMARK:
                    markTask(Integer.parseInt(arguments) - 1, false, tasks, ui, storage);
                    break;

                case TODO:
                    if (arguments == null) {
                        throw new OrionException("You must provide a description for the task!");
                    }
                    addTask(new Todo(arguments), tasks, ui, storage);
                    break;

                case DEADLINE:
                    if (arguments == null) {
                        throw new OrionException("You must provide a description and deadline!");
                    }
                    String[] deadlineParts = arguments.split(" /by ");
                    if (deadlineParts.length == 1) {
                        throw new OrionException("You must provide the deadline of the task in " +
                                "this format:\n" +
                                "\"deadline (description) /by (date)\"");
                    }
                    addTask(new Deadline(deadlineParts[0], parseDateTime(deadlineParts[1])), tasks, ui, storage);
                    break;

                case EVENT:
                    if (arguments == null) {
                        throw new OrionException("You must provide a description and start/end!");
                    }
                    String[] eventParts = arguments.split(" /from ");
                    if (eventParts.length == 1) {
                        throw new OrionException("You must specify the details of the event in " +
                                "this format:\n" +
                                "\"event (description) /from (date) /to (date)\"");
                    }
                    String[] timeParts = eventParts[1].split(" /to ");
                    if (timeParts.length == 1) {
                        throw new OrionException("You must specify the details of the event in " +
                                "this format:\n" +
                                "\"event (description) /from (date) /to (date)\"");
                    }
                    addTask(new Event(eventParts[0], parseDateTime(timeParts[0]), parseDateTime(timeParts[1])), tasks, ui, storage);
                    break;

                case DELETE:
                    deleteTask(Integer.parseInt(arguments) - 1, tasks, ui, storage);
                    break;
                    
                case FIND:
                    if (arguments == null) {
                        throw new OrionException("You must provide a keyword to search for!");
                    }
                    findTask(arguments, tasks, ui);

                default:
                    throw new OrionException("That is not a valid command!");
            }
        } catch (OrionException e) {
            ui.showError(e.getMessage());
        } catch (NumberFormatException e) {
            ui.showError("Could not parse the task number you provided!");
        } catch (IndexOutOfBoundsException e) {
            ui.showError("You do not have a task with the number you provided!");
        }
        return true;
    }

    private static void addTask(Task task, TaskList tasks, Ui ui, Storage storage) {
        tasks.addTask(task);

        ui.showLine();
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("  " + task);
        ui.showMessage("You now have " + tasks.getSize() + " task(s) in the list.");
        ui.showLine();

        try {
            storage.save(tasks.getTasks());
        } catch (OrionException e) {
            ui.showError(e.getMessage());
        }
    }

    private static void markTask(int index, boolean isDone, TaskList tasks, Ui ui, Storage storage) {
        Task task = tasks.getTask(index);
        task.setDone(isDone);

        ui.showLine();
        ui.showMessage(isDone ? "Nice! I've marked this task as done:" : "OK, I've marked this task as not done yet:");
        ui.showMessage("  " + task);
        ui.showLine();

        try {
            storage.save(tasks.getTasks());
        } catch (OrionException e) {
            ui.showError(e.getMessage());
        }
    }

    private static void deleteTask(int index, TaskList tasks, Ui ui, Storage storage) {
        Task task = tasks.removeTask(index);

        ui.showLine();
        ui.showMessage("Got it. I've removed this task:");
        ui.showMessage("  " + task);
        ui.showMessage("You now have " + tasks.getSize() + " task(s) in the list.");
        ui.showLine();

        try {
            storage.save(tasks.getTasks());
        } catch (OrionException e) {
            ui.showError(e.getMessage());
        }
    }

    private static LocalDateTime parseDateTime(String input) {
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(input, formatter);
            } catch (DateTimeParseException e) {
                try {
                    return LocalDate.parse(input, formatter).atStartOfDay();
                } catch (DateTimeParseException ex) {

                }
            }
        }
        throw new OrionException("Invalid date format! Try: yyyy-mm-dd or dd/mm/yyyy\n" +
                "(Specifying time in 24h format is optional)");
    }

    private static void findTask(String keyword, TaskList tasks, Ui ui) {
        ui.showLine();
        ui.showMessage("Here are the matching tasks in your list:");

        int count = 1;
        for (Task task : tasks.getTasks()) {
            if (task.getDescription().contains(keyword)) {
                ui.showMessage(count++ + ". " + task);
            }
        }

        ui.showLine();
    }
}