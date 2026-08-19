import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Orion {
    private static final String BANNER = "  ____       _\n"
            + " / __ \\_____(_)___  ____\n"
            + "/ /_/ / ___/ / __ \\/ __ \\\n"
            + "\\____/_/  /_/\\____/_/ /_/\n";
    private static final String LINE = "____________________________________________________________";

    private static List<String> tasks = new ArrayList<>();

    public static void main(String[] args) {
        greet();
        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        while (running) {
            System.out.print("\n> ");
            String command = scanner.nextLine();
            switch (command) {
                case "list":
                    listTasks();
                    break;

                case "bye":
                    bye();
                    running = false;
                    break;

                default:
                    addTask(command);
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

    private static void addTask(String task) {
        System.out.println(LINE);
        tasks.add(task);
        System.out.println("added: " + task);
        System.out.println(LINE);
    }

    private static void listTasks() {
        System.out.println(LINE);
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
        System.out.println(LINE);
    }
}
