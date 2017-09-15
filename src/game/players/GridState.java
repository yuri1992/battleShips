package game.players;

public enum GridState {
    EMPTY("EMPTY"),
    OCCUPIED("OCCUPIED");

    private final String text;
    private GridState(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}