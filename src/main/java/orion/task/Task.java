package orion.task;

import java.time.format.DateTimeFormatter;

/**
 * Represents a general task with a description and a completion status.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    protected static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");

    /**
     * Initializes a new Task with the given description.
     * The task is marked as not done by default.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this(description, false);
    }

    /**
     * Initializes a new Task with the given description and completion status.
     *
     * @param description The description of the task.
     * @param isDone The completion status of the task.
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }

    public abstract String getTypeIcon();

    /**
     * Returns the formatted string representation of the task for saving to a file.
     *
     * @return The formatted data string.
     */
    public String toFileFormat() {
        return getTypeIcon() + " | " + (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns the string representation of the task for displaying to the user.
     *
     * @return The string representation of the task.
     */
    @Override
    public String toString() {
        return "[" + getTypeIcon() + "][" + getStatusIcon() + "] " + description;
    }
}
