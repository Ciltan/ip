public class Event extends Task {
    protected String start;
    protected String end;

    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    public Event(String description, String start, String end, boolean isDone) {
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
        return super.toFileFormat() + " | " + start + " | " + end;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + start + ", to: " + end + ")";
    }
}
