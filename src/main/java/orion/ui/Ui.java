package orion.ui;

import java.util.Scanner;

/**
 * Handles all user interface interactions, including reading input and displaying messages.
 */
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

    /**
     * Reads the next line of input from the user.
     *
     * @return The raw command string entered by the user.
     */
    public String readCommand() {
        System.out.print("\n> ");
        return scanner.nextLine();
    }

    /**
     * Prints the welcome message and banner to the user.
     */
    public void showWelcome() {
        System.out.println(LINE);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Orion, your friendly chatbot.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Prints the goodbye message to the user upon exiting.
     */
    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println("Hope to see you again soon. Goodbye!");
        System.out.println(LINE);
    }

    /**
     * Prints a standardized line separator to the console.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Prints an error message to the user.
     *
     * @param message The specific error message to be displayed.
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    /**
     * Prints a standard message to the user.
     *
     * @param message The text to be displayed.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }
}
