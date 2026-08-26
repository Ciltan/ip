import java.time.LocalDateTime;

public class Event extends Task {
    protected LocalDateTime start;
    protected LocalDateTime end;

    public Event(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    public Event(String description, LocalDateTime start, LocalDateTime end, boolean isDone) {
        super(description, isDone);
        this.start = start;
        this.end = end;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String toFileFormat() {
        return super.toFileFormat() + " | " + start.format(Storage.SAVE_FORMAT) + " | " + end.format(Storage.SAVE_FORMAT);
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + start.format(OUTPUT_FORMAT) + "; to: " + end.format(OUTPUT_FORMAT) + ")";
    }
}
