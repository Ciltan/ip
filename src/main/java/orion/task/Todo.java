package orion.task;

/**
 * Represents a task without any specific date or time constraints.
 */
public class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    public Todo(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String getTypeIcon() {
        return "T";
    }
}
