import java.util.Scanner;

public class Orion {
    private static final String BANNER = "  ____       _\n"
            + " / __ \\_____(_)___  ____\n"
            + "/ /_/ / ___/ / __ \\/ __ \\\n"
            + "\\____/_/  /_/\\____/_/ /_/\n";
    private static final String LINE = "____________________________________________________________";

    public static void main(String[] args) {
        greet();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\n> ");
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                bye();
                break;
            } else {
                System.out.println(LINE);
                System.out.println(command);
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
}
