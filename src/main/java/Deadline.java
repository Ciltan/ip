import java.time.LocalDateTime;

public class Deadline extends Task {
    protected LocalDateTime deadline;

    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
    }

    public Deadline(String description, LocalDateTime deadline, boolean isDone) {
        super(description, isDone);
        this.deadline = deadline;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + " | " + deadline.format(Storage.SAVE_FORMAT);
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + deadline.format(OUTPUT_FORMAT) + ")";
    }
}
