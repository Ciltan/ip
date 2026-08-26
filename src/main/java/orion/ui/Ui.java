package orion.ui;

import java.util.Scanner;

public class Ui {
    private static final String BANNER = "  ____       _\n"
            + " / __ \\_____(_)___  ____\n"
            + "/ /_/ / ___/ / __ \\/ __ \\\n"
            + "\\____/_/  /_/\\____/_/ /_/\n";
    private static final String LINE = "____________________________________________________________";
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        System.out.print("\n> ");
        return scanner.nextLine();
    }

    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Orion, your friendly chatbot.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println("Hope to see you again soon. Goodbye!");
        System.out.println(LINE);
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}