import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Orion {
    private static final String BANNER = "  ____       _\n"
            + " / __ \\_____(_)___  ____\n"
            + "/ /_/ / ___/ / __ \\/ __ \\\n"
            + "\\____/_/  /_/\\____/_/ /_/\n";
    private static final String LINE = "____________________________________________________________";

    private static List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        greet();
        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        while (running) {
            try {
                System.out.print("\n> ");
                String input = scanner.nextLine();
                String[] parts = input.split(" ", 2);
                Command command = Command.valueOf(parts[0].toUpperCase());
                String arguments = parts.length == 2 ? parts[1] : null;
                switch (command) {
                    case LIST:
                        listTasks();
                        break;

                    case BYE:
                        bye();
                        running = false;
                        break;

                    case MARK:
                        markTask(Integer.parseInt(arguments) - 1, true);
                        break;

                    case UNMARK:
                        markTask(Integer.parseInt(arguments) - 1, false);
                        break;

                    case TODO:
                        if (arguments == null) {
                            throw new OrionException("You must provide a description for the task!");
                        }
                        addTask(new Todo(arguments));
                        break;

                    case DEADLINE:
                        if (arguments == null) {
                            throw new OrionException("You must provide a description and deadline for the task!");
                        }
                        String[] deadlineParts = arguments.split(" /by ");
                        if (deadlineParts.length == 1) {
                            throw new OrionException("You must provide the deadline of the task in " +
                                    "this format:\n" +
                                    "\"deadline (description) /by (deadline)\"");
                        }
                        addTask(new Deadline(deadlineParts[0], deadlineParts[1]));
                        break;

                    case EVENT:
                        if (arguments == null) {
                            throw new OrionException("You must provide a description and start/end for the event!");
                        }
                        String[] eventParts = arguments.split(" /from ");
                        if (eventParts.length == 1) {
                            throw new OrionException("You must specify the details of the event in " +
                                    "this format:\n" +
                                    "\"event (description) /from (start) /to (end)\"");
                        }
                        String[] timeParts = eventParts[1].split(" /to ");
                        if (timeParts.length == 1) {
                            throw new OrionException("You must specify the details of the event in " +
                                    "this format:\n" +
                                    "\"event (description) /from (start) /to (end)\"");
                        }
                        addTask(new Event(eventParts[0], timeParts[0], timeParts[1]));
                        break;

                    case DELETE:
                        deleteTask(Integer.parseInt(arguments) - 1);
                        break;
                }
            } catch (OrionException e) {
                System.out.println(LINE);
                System.out.println(e.getMessage());
                System.out.println(LINE);
            } catch (NumberFormatException e) {
                System.out.println(LINE);
                System.out.println("Could not parse the task number you provided!");
                System.out.println(LINE);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("You do not have a task with the number you provided!");
                System.out.println(LINE);
            } catch (IllegalArgumentException e) {
                System.out.println(LINE);
                System.out.println("That is not a valid command!");
                System.out.println(LINE);
            }
        }
    }

    private static void greet() {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Orion, your friendly chatbot.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    private static void bye() {
        System.out.println(LINE);
        System.out.println("Hope to see you again soon. Goodbye!");
        System.out.println(LINE);
    }

    private static void addTask(Task task) {
        System.out.println(LINE);
        tasks.add(task);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("You now have " + tasks.size() + " task(s) in the list.");
        System.out.println(LINE);
    }

    private static void listTasks() {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println(LINE);
    }

    private static void markTask(int index, boolean isDone) {
        System.out.println(LINE);
        Task task = tasks.get(index);
        task.setDone(isDone);
        if (isDone) {
            System.out.println("Nice! I've marked this task as done:");
        } else {
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println("  " + task);
        System.out.println(LINE);
    }

    private static void deleteTask(int index) {
        System.out.println(LINE);
        Task task = tasks.remove(index);
        System.out.println("Got it. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("You now have " + tasks.size() + " task(s) in the list.");
        System.out.println(LINE);
    }
}
