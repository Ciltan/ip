public class Deadline extends Task {
    protected String deadline;

    public Deadline(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    public Deadline(String description, String deadline, boolean isDone) {
        super(description, isDone);
        this.deadline = deadline;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + " | " + deadline;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + deadline + ")";
    }
}
