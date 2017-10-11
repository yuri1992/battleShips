package game.model.boards;

public enum GridState {
    EMPTY("EMPTY"),
    OCCUPIED("OCCUPIED");

    private final String text;
    GridState(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
