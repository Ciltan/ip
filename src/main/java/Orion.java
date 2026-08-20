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
            System.out.print("\n> ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ", 2);
            String command = parts[0];
            String arguments = parts.length == 2 ? parts[1] : null;
            switch (command) {
                case "list":
                    listTasks();
                    break;

                case "bye":
                    bye();
                    running = false;
                    break;

                case "mark":
                    markTask(Integer.parseInt(arguments) - 1, true);
                    break;

                case "unmark":
                    markTask(Integer.parseInt(arguments) - 1, false);
                    break;

                case "todo":
                    addTask(new Todo(arguments));
                    break;

                case "deadline":
                    String[] deadlineParts = arguments.split(" /by ");
                    addTask(new Deadline(deadlineParts[0], deadlineParts[1]));
                    break;

                case "event":
                    String[] eventParts = arguments.split(" /from ");
                    String[] timeParts = eventParts[1].split(" /to ");
                    addTask(new Event(eventParts[0], timeParts[0], timeParts[1]));
                    break;
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
}
