package orion.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import orion.exception.OrionException;
import orion.format.ResponseFormatter;
import orion.storage.Storage;
import orion.task.Deadline;
import orion.task.Event;
import orion.task.Task;
import orion.task.TaskList;
import orion.task.Todo;

/**
 * Handles the parsing of user input and execution of commands.
 */
public class Parser {

    /**
     * Parses the user input and executes the corresponding command.
     *
     * @param input Raw command string entered by the user.
     * @param tasks TaskList containing the current tasks.
     * @param formatter ResponseFormatter object to handle text formatting.
     * @param storage Storage object to handle saving data.
     * @return The formatted response string to display to the user.
     */
    public static String parseAndExecute(String input, TaskList tasks, ResponseFormatter formatter, Storage storage) {
        assert input != null : "Command input string should not be null";
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
                    return formatter.getTaskListMessage(tasks);

                case BYE:
                    return formatter.getGoodbyeMessage();

                case MARK:
                    return markTask(Integer.parseInt(arguments) - 1, true, tasks, formatter, storage);

                case UNMARK:
                    return markTask(Integer.parseInt(arguments) - 1, false, tasks, formatter, storage);

                case TODO:
                    if (arguments == null || arguments.isBlank()) {
                        throw new OrionException("You must provide a description for the task!");
                    }
                    return addTask(new Todo(arguments), tasks, formatter, storage);

                case DEADLINE:
                    return addTask(createDeadline(arguments), tasks, formatter, storage);

                case EVENT:
                    return addTask(createEvent(arguments), tasks, formatter, storage);

                case DELETE:
                    return deleteTask(Integer.parseInt(arguments) - 1, tasks, formatter, storage);

                case FIND:
                    if (arguments == null || arguments.isBlank()) {
                        throw new OrionException("You must provide a keyword to search for!");
                    }
                    return findTask(arguments, tasks, formatter);

                default:
                    throw new OrionException("That is not a valid command!");
            }
        } catch (OrionException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return "Could not parse the task number you provided!";
        }
    }

    /**
     * Checks if the given user input is an exit command.
     *
     * @param input Raw command string entered by the user.
     * @return {@code true} if the command is the exit command, {@code false} otherwise.
     */
    public static boolean isExit(String input) {
        if (input == null || input.isBlank()) {
            return false;
        }
        String[] parts = input.split(" ", 2);
        try {
            return Command.valueOf(parts[0].toUpperCase()) == Command.BYE;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static String addTask(Task task, TaskList tasks, ResponseFormatter formatter, Storage storage) {
        assert task != null : "Task being added should not be null";
        tasks.addTask(task);
        storage.save(tasks.getTasks());
        return formatter.getTaskAddedMessage(task, tasks.getSize());
    }

    private static String markTask(int index, boolean isDone, TaskList tasks,
                                   ResponseFormatter formatter, Storage storage) {
        assert index >= 0 : "Task index should not be negative";
        try {
            Task task = tasks.getTask(index);
            task.setDone(isDone);
            storage.save(tasks.getTasks());
            return formatter.getTaskMarkedMessage(task, isDone);
        } catch (IndexOutOfBoundsException e) {
            return "You do not have a task with the number you provided!";
        }
    }

    private static String deleteTask(int index, TaskList tasks, ResponseFormatter formatter, Storage storage) {
        assert index >= 0 : "Task index should not be negative";
        try {
            Task task = tasks.removeTask(index);
            storage.save(tasks.getTasks());
            return formatter.getTaskDeletedMessage(task, tasks.getSize());
        } catch (IndexOutOfBoundsException e) {
            return "You do not have a task with the number you provided!";
        }
    }

    private static String findTask(String keyword, TaskList tasks, ResponseFormatter formatter) {
        return formatter.getMatchingTasksMessage(keyword, tasks);
    }

    private static Deadline createDeadline(String arguments) {
        if (arguments == null || arguments.isBlank()) {
            throw new OrionException("You must provide a description and deadline!");
        }
        String[] deadlineParts = arguments.split(" /by ");
        if (deadlineParts.length == 1) {
            throw new OrionException("You must provide the deadline of the task in this format:\n"
                    + "\"deadline (description) /by (date)\"");
        }
        return new Deadline(deadlineParts[0], parseDateTime(deadlineParts[1]));
    }

    private static Event createEvent(String arguments) {
        if (arguments == null || arguments.isBlank()) {
            throw new OrionException("You must provide a description and start/end!");
        }
        String[] eventParts = arguments.split(" /from ");
        if (eventParts.length == 1) {
            throw new OrionException("You must specify the details of the event in this format:\n"
                    + "\"event (description) /from (date) /to (date)\"");
        }
        String[] timeParts = eventParts[1].split(" /to ");
        if (timeParts.length == 1) {
            throw new OrionException("You must specify the details of the event in this format:\n"
                    + "\"event (description) /from (date) /to (date)\"");
        }
        String description = eventParts[0];
        LocalDateTime startTime = parseDateTime(timeParts[0]);
        LocalDateTime endTime = parseDateTime(timeParts[1]);
        return new Event(description, startTime, endTime);
    }

    private static LocalDateTime parseDateTime(String input) {
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
        };
        for (DateTimeFormatter pattern : formatters) {
            try {
                return LocalDateTime.parse(input, pattern);
            } catch (DateTimeParseException e) {
                try {
                    return LocalDate.parse(input, pattern).atStartOfDay();
                } catch (DateTimeParseException ex) {
                    // Input did not match the formatter's pattern, ignore the exception and continue with the next one
                }
            }
        }
        throw new OrionException("Invalid date format! Try: yyyy-mm-dd or dd/mm/yyyy\n"
                + "(Specifying time in 24h format is optional)");
    }
}
